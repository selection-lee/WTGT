package com.ssafy.wattagatta.domain.agent.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.wattagatta.domain.agent.dto.response.AgentDataResponse;
import com.ssafy.wattagatta.domain.agent.dto.response.AgentPositionResponse;
import com.ssafy.wattagatta.domain.agent.dto.response.AgentRouteResponse;
import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.agent.model.AgentStatus;
import com.ssafy.wattagatta.domain.agent.model.Constraint;
import com.ssafy.wattagatta.domain.agent.model.ConveyPath;
import com.ssafy.wattagatta.domain.agent.model.Direction;
import com.ssafy.wattagatta.domain.agent.model.Node;
import com.ssafy.wattagatta.domain.agent.service.PathCalcService;
import com.ssafy.wattagatta.domain.agent.utils.PathStore;
import com.ssafy.wattagatta.domain.product.dto.TargetLoc;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import com.ssafy.wattagatta.global.utils.GlobalClock;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AgentManager {

    private final List<Agent> agents;
    private final PathStore pathStore;
    private final GlobalClock globalClock;
    private final PathCalcService pathCalcService;
    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager webSocketSessionManager;

    private Agent agent1 = new Agent();
    private Agent agent2 = new Agent();

    @Value("${agent.task.duration.time}")
    private int TASK_DURATION_TIME;

    @Value("${agent.move.duration.time}")
    private int MOVE_DURATION_TIME;

    @Value("${websocket.unity.path}")
    private String unityPath;

    @Value("${websocket.agent.path}")
    private String agentPath;

    public AgentManager(PathCalcService pathCalcService, GlobalClock globalClock,
                        WebSocketSessionManager webSocketSessionManager) {
        this.globalClock = globalClock;
        this.pathCalcService = pathCalcService;
        this.agents = new ArrayList<>();
        this.pathStore = new PathStore();
        objectMapper = new ObjectMapper();
        agent1.ready("agent1", new Node(0, 0, Direction.EAST));
        agent2.ready("agent2", new Node(1, 0, Direction.EAST));
        agents.add(agent1);
        agents.add(agent2);
        this.webSocketSessionManager = webSocketSessionManager;
    }

    public synchronized void assignTaskToAgent(Agent agent, TargetLoc targetLoc, Consumer<TargetLoc> failureCallback) {
        try {
            int currentGlobalTime = globalClock.getGlobalTime();
            agent.assignTask(targetLoc, currentGlobalTime);

            // 작업 경로 계산
            List<Constraint> constraints = pathStore.getConstraintsForAgent(agent.getId());

            ConveyPath conveyPath = calcAgentPathToTarget(agent, constraints);
            List<Node> pathToTarget = conveyPath.getPath();
            int conveyPathSize = conveyPath.getConveyPathSize();

            // 복귀 경로 계산
            List<Node> returnPath = calcAgentReturnPath(agent, constraints);

            log.info("에이전트 {}의 경로: {}", agent.getId(), pathToTarget);
            log.info("에이전트 {}의 복귀 경로 : {}", agent.getId(), returnPath);

            List<Node> fullPath = assignFullPathToAgent(agent, pathToTarget, returnPath);

            pathStore.savePath(agent.getId(), currentGlobalTime, fullPath);
            log.info("에이전트 전체 설정 경로 : {}", agent.getCurrentPath());

            int targetPathSize = pathToTarget.size();
            sendAgentRoute(agent, fullPath, conveyPathSize, targetPathSize);

            simulateAgentMovement(agent, fullPath, pathToTarget.size(), conveyPathSize);
        } catch (CustomException e) {
            log.error("에이전트 {}가 경로를 찾지 못했습니다: {}", agent.getId(), e.getMessage());
            agent.setStatus(AgentStatus.IDLE);
            failureCallback.accept(targetLoc);
        }
    }

    private void simulateAgentMovement(Agent agent, List<Node> fullPath, int pathToTargetSize, int conveyPathSize) {
        new Thread(() -> {
            try {
                Node previousNode = null;
                for (int i = 0; i < fullPath.size(); i++) {
                    log.info("simulate i : {}, fullPath.size : {}", i, fullPath.size());
                    Node currentNode = fullPath.get(i);

                    Thread.sleep(1000);

                    int angle = 1;
                    if (previousNode != null) {
                        angle = calculateAngle(previousNode, currentNode);
                    }

                    boolean arrived = false;
                    boolean conveyArrived = false;
                    boolean houseArrived = false;
                    if (i == pathToTargetSize - (TASK_DURATION_TIME + 1)) {
                        arrived = true;
                    }
                    if (i == fullPath.size() - (TASK_DURATION_TIME + 1)) {
                        houseArrived = true;
                    }
                    if (i == conveyPathSize - (TASK_DURATION_TIME + 1)) {
                        conveyArrived = true;
                    }

                    sendAgentLocationUpdate(agent, currentNode, angle, arrived, conveyArrived, houseArrived);

                    agent.setCurrentNode(currentNode);
                    previousNode = currentNode;
                }
            } catch (InterruptedException e) {
                log.error("에이전트 이동 시뮬레이션 중 예외 발생", e);
                Thread.currentThread().interrupt();
            } finally {
                agent.setStatus(AgentStatus.IDLE);
                pathStore.removePath(agent.getId());
                notifyAgentAvailable();
                log.info("에이전트 {}가 작업 및 복귀를 완료했습니다.", agent.getId());
            }
        }).start();
    }


    private ConveyPath calcAgentPathToTarget(Agent agent, List<Constraint> constraints) {
        Node originalGoalNode = agent.getGoalNode();
        Node originalCurrentNode = agent.getCurrentNode();

        agent.setGoalNode(agent.getConveyNode());
        List<Node> pathToConvey = pathCalcService.calcPath(agent, constraints);

        if (pathToConvey == null) {
            agent.setStatus(AgentStatus.IDLE);
            throw new CustomException(ErrorCode.CANNOT_FIND_NEW_PATH);
        }

        agent.setCurrentNode(agent.getConveyNode());
        agent.setGoalNode(originalGoalNode);
        List<Node> pathFromConveyToGoal = pathCalcService.calcPath(agent, constraints);
        if (pathFromConveyToGoal == null) {
            agent.setStatus(AgentStatus.IDLE);
            throw new CustomException(ErrorCode.CANNOT_FIND_NEW_PATH);
        }

        if (!pathFromConveyToGoal.isEmpty() && pathFromConveyToGoal.get(0)
                .equals(pathToConvey.get(pathToConvey.size() - 1))) {
            pathFromConveyToGoal.remove(0);
        }

        List<Node> fullPath = new ArrayList<>();
        fullPath.addAll(pathToConvey);
        fullPath.addAll(pathFromConveyToGoal);

        agent.setCurrentNode(originalCurrentNode);

        return new ConveyPath(fullPath, pathToConvey.size());
    }


    private List<Node> calcAgentReturnPath(Agent agent, List<Constraint> constraints) {
        agent.setCurrentNode(agent.getGoalNode());
        agent.setGoalNode(agent.getHomeNode());

        List<Node> returnPath = pathCalcService.calcPath(agent, constraints);
        if (returnPath == null) {
            agent.setStatus(AgentStatus.IDLE);
            throw new CustomException(ErrorCode.CANNOT_FIND_NEW_PATH);
        }

        return returnPath;
    }

    private List<Node> assignFullPathToAgent(Agent agent, List<Node> pathToTarget, List<Node> returnPath) {
        List<Node> fullPath = new ArrayList<>(pathToTarget);
        fullPath.addAll(returnPath);
        agent.setCurrentPath(fullPath);
        return fullPath;
    }


    private List<Node> calcAgentPath(Agent agent) {
        List<Constraint> homePathConstraints = pathStore.getConstraintsForAgent(agent.getId());
        List<Node> homePath = pathCalcService.calcPath(agent, homePathConstraints);
        if (homePath == null) {
            agent.setStatus(AgentStatus.IDLE);
            throw new CustomException(ErrorCode.CANNOT_FIND_NEW_PATH);
        }
        agent.setCurrentPath(new ArrayList<>(homePath));
        return homePath;
    }

    public synchronized Agent findAvailableAgent() {
        return agents.stream()
                .filter(Agent::isAvailable)
                .findFirst()
                .orElse(null);
    }

    public synchronized Agent waitForAvailableAgent() throws InterruptedException {
        Agent agent;
        while ((agent = findAvailableAgent()) == null) {
            wait();
        }
        return agent;
    }

    public synchronized void notifyAgentAvailable() {
        notifyAll();
        log.info("agent 작업 완료 notify All");
    }

    private void sendAgentLocationUpdate(Agent agent, Node nextNode, int angle, boolean arrived,
                                         boolean conveyArrived, boolean houseArrived) {
        try {
            AgentPositionResponse rcCarPosition = new AgentPositionResponse(
                    nextNode.getX(),
                    -2,
                    nextNode.getY()
            );
            AgentDataResponse response = new AgentDataResponse(
                    Integer.parseInt(agent.getId().replace("agent", "")),
                    rcCarPosition,
                    angle,
                    arrived,
                    conveyArrived,
                    houseArrived
            );

            String jsonMessage = objectMapper.writeValueAsString(response);
//            log.info("Unity 전송 데이터 : {}", jsonMessage);
            if (!webSocketSessionManager.sendMessageToPath(unityPath, jsonMessage)) {
                log.error("메시지 전송 실패");
            }
        } catch (Exception e) {
            log.error("에이전트 위치 전송 실패", e);
        }
    }

    /**
     * RC Car 메시지 전송 위한 메서드
     * @param agent
     * @param fullPath
     * @param conveyPathSize
     * @param targetPathSize
     */
    private void sendAgentRoute(Agent agent, List<Node> fullPath, int conveyPathSize, int targetPathSize) {
        try {
            int carNumber = Integer.parseInt(agent.getId().replace("agent", ""));

            List<Integer> routeActions = generateRouteActions(fullPath);

            int conveyWaitTime = TASK_DURATION_TIME;
            int targetWaitTime = TASK_DURATION_TIME;
            int homeWaitTime = TASK_DURATION_TIME;

            int conveyPathEndIndex = conveyPathSize - 1 - conveyWaitTime;
            int targetPathStartIndex = conveyPathEndIndex + TASK_DURATION_TIME;
            int targetPathEndIndex = targetPathSize - 1 - targetWaitTime;
            int returnHomeStartIndex = targetPathEndIndex + TASK_DURATION_TIME;
            int routeActionsSize = routeActions.size() - 1 - homeWaitTime;

            List<Integer> routeToConvey = routeActions.subList(0, conveyPathEndIndex);
            List<Integer> routeToTarget = routeActions.subList(targetPathStartIndex, targetPathEndIndex);
            List<Integer> routeToHome = routeActions.subList(returnHomeStartIndex, routeActionsSize);

            Direction startingDirectionToConvey = agent.getCurrentDirection();
            Direction startingDirectionToTarget = Direction.NORTH;
            Direction startingDirectionToHome = Direction.SOUTH;

            routeToConvey = adjustRoute(routeToConvey, fullPath, startingDirectionToConvey, 0);
            routeToTarget = adjustRoute(routeToTarget, fullPath, startingDirectionToTarget, targetPathStartIndex);
            routeToHome = adjustRoute(routeToHome, fullPath, startingDirectionToHome, returnHomeStartIndex);

            List<Integer> abRouteToConvey = generateAbsoluteDirections(routeToConvey, startingDirectionToConvey);
            List<Integer> abRouteToTarget = generateAbsoluteDirections(routeToTarget, startingDirectionToTarget);
            List<Integer> abRouteToHome = generateAbsoluteDirections(routeToHome, startingDirectionToHome);

            sendRouteSegment(carNumber, 1, routeToConvey, abRouteToConvey);
            sendRouteSegment(carNumber, 2, routeToTarget, abRouteToTarget);
            sendRouteSegment(carNumber, 3, routeToHome, abRouteToHome);

        } catch (Exception e) {
            log.error("RCcar 메시지 전송 실패", e);
        }
    }

    private void sendRouteSegment(int carNumber, int goal, List<Integer> routeSegment, List<Integer> abRouteSegment) {
        try {
            AgentRouteResponse response = new AgentRouteResponse(carNumber, goal, routeSegment, abRouteSegment);
            String jsonMessage = objectMapper.writeValueAsString(response);

            if (!webSocketSessionManager.sendMessageToPath(agentPath, jsonMessage)) {
                log.error("RCcar 메시지 전송 실패");
            }
        } catch (Exception e) {
            log.error("RCcar 메시지 전송 실패", e);
        }
    }


    private int calculateAngle(Node currentNode, Node nextNode) {
        if (currentNode.getDirection() == nextNode.getDirection()) {
            return 1; // 직진
        } else if (isRightTurn(currentNode.getDirection(), nextNode.getDirection())) {
            return 2; // 우회전
        } else {
            return 0; // 좌회전
        }
    }

    private List<Integer> generateRouteActions(List<Node> path) {
        List<Integer> routeActions = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            Node currentNode = path.get(i);
            Node nextNode = path.get(i + 1);

            int action = calculateAction(currentNode, nextNode);
            routeActions.add(action);
        }

        routeActions.add(0);

        return routeActions;
    }

    private int calculateAction(Node currentNode, Node nextNode) {
        Direction currentDirection = currentNode.getDirection();
        Direction nextDirection = nextNode.getDirection();

        if (currentDirection == nextDirection) {
            return 1;
        } else if (isLeftTurn(currentDirection, nextDirection)) {
            return 2;
        } else if (isRightTurn(currentDirection, nextDirection)) {
            return 3;
        } else {
            return 0;
        }
    }


    /**
     * Rc car 방향 전환 고려한 경로
     * @param routeSegment
     * @param fullPath
     * @param desiredStartingDirection
     * @param segmentStartIndex
     * @return
     */
    private List<Integer> adjustRoute(
            List<Integer> routeSegment, List<Node> fullPath, Direction desiredStartingDirection, int segmentStartIndex) {

        if (routeSegment.isEmpty() || segmentStartIndex >= fullPath.size() - 1) {
            return routeSegment;
        }

        int index = 0;
        Direction currentDirection = desiredStartingDirection;

        for (int i = segmentStartIndex; i < fullPath.size() - 1 && index < routeSegment.size(); i++) {
            Node currentNode = fullPath.get(i);
            Node nextNode = fullPath.get(i + 1);

            Direction requiredDirection = calculateDirection(currentNode, nextNode);

            if (currentDirection != requiredDirection) {
                if (routeSegment.get(index) == 2 || routeSegment.get(index) == 3) {
                    index++;
                    currentDirection = requiredDirection;
                }
                // 방향 전환 명령이 아니면 루프 종료
                else break;

            }
            // 원하는 방향으로 설정 완료
            else break;

        }

        return routeSegment.subList(index, routeSegment.size());
    }

    /**
     * 절대 방향
     * @param routeSegment
     * @param startingDirection
     * @return
     */
    private List<Integer> generateAbsoluteDirections(List<Integer> routeSegment, Direction startingDirection) {
        List<Integer> abRoute = new ArrayList<>();
        Direction currentDirection = startingDirection;

        for (Integer action : routeSegment) {
            abRoute.add(directionToInt(currentDirection));

            switch (action) {
                case 2:
                    currentDirection = currentDirection.turnLeft();
                    break;
                case 3:
                    currentDirection = currentDirection.turnRight();
                    break;
                default:
                    break;
            }
        }
        return abRoute;
    }

    private int directionToInt(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> -1;
        };
    }


    private Direction calculateDirection(Node fromNode, Node toNode) {
        int dx = toNode.getX() - fromNode.getX();
        int dy = toNode.getY() - fromNode.getY();

        if (dx == 1 && dy == 0) {
            return Direction.NORTH;
        } else if (dx == -1 && dy == 0) {
            return Direction.SOUTH;
        } else if (dx == 0 && dy == 1) {
            return Direction.EAST;
        } else if (dx == 0 && dy == -1) {
            return Direction.WEST;
        }
        return fromNode.getDirection();
    }


    private boolean isLeftTurn(Direction current, Direction next) {
        return (current == Direction.NORTH && next == Direction.WEST)
                || (current == Direction.WEST && next == Direction.SOUTH)
                || (current == Direction.SOUTH && next == Direction.EAST)
                || (current == Direction.EAST && next == Direction.NORTH);
    }

    private boolean isRightTurn(Direction current, Direction next) {
        return (current == Direction.NORTH && next == Direction.EAST)
                || (current == Direction.EAST && next == Direction.SOUTH)
                || (current == Direction.SOUTH && next == Direction.WEST)
                || (current == Direction.WEST && next == Direction.NORTH);
    }



}
