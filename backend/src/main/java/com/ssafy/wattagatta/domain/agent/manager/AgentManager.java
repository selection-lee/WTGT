package com.ssafy.wattagatta.domain.agent.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.wattagatta.domain.agent.dto.response.AgentDataResponse;
import com.ssafy.wattagatta.domain.agent.dto.response.AgentPositionResponse;
import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.agent.model.AgentStatus;
import com.ssafy.wattagatta.domain.agent.model.Constraint;
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
    private String path;

    public AgentManager(PathCalcService pathCalcService, GlobalClock globalClock,
                        WebSocketSessionManager webSocketSessionManager) {
        this.globalClock = globalClock;
        this.pathCalcService = pathCalcService;
        this.agents = new ArrayList<>();
        this.pathStore = new PathStore();
        objectMapper = new ObjectMapper();
        agent1.ready("agent1", new Node(1, 0, Direction.EAST));
        agent2.ready("agent2", new Node(0, 1, Direction.EAST));
        agents.add(agent1);
        agents.add(agent2);
        this.webSocketSessionManager = webSocketSessionManager;
    }

    public synchronized void assignTaskToAgent(Agent agent, TargetLoc targetLoc) {
        int currentGlobalTime = globalClock.getGlobalTime();
        agent.assignTask(targetLoc, currentGlobalTime);

        // 작업 경로 계산
        List<Constraint> constraints = pathStore.getConstraintsForAgent(agent.getId());
        List<Node> pathToTarget = calcAgentPathToTarget(agent, currentGlobalTime, constraints);

        // 작업 수행 시간 계산
        int arrivalTimeAtTarget = currentGlobalTime + pathToTarget.size();
        reserveTaskLocationAndUpdateConstraints(agent, arrivalTimeAtTarget, constraints);

        // 복귀 경로 계산
        int startTimeForReturn = arrivalTimeAtTarget + TASK_DURATION_TIME;
        List<Node> returnPath = calcAgentReturnPath(agent, startTimeForReturn, constraints);

        log.info("에이전트 {}의 경로: {}", agent.getId(), pathToTarget);
        log.info("에이전트 {}의 복귀 경로 : {}", agent.getId(), returnPath);

        List<Node> fullPath = assignFullPathToAgent(agent, pathToTarget, returnPath);
        pathStore.savePath(agent.getId(), currentGlobalTime, fullPath);

        simulateAgentMovement(agent, fullPath, pathToTarget.size());
    }

    private void simulateAgentMovement(Agent agent, List<Node> fullPath, int pathToTargetSize) {
        new Thread(() -> {
            try {
                Node previousNode = null;
                for (int i = 0; i < fullPath.size(); i++) {
                    Node currentNode = fullPath.get(i);

                    Thread.sleep(1000);

                    int angle = 0;
                    if (previousNode != null) {
                        angle = calculateAngle(previousNode, currentNode);
                    }

                    boolean arrived = false;
                    if (i == pathToTargetSize - 1 || i == fullPath.size() - 1) {
                        arrived = true;
                    }

                    sendAgentLocationUpdate(agent, currentNode, angle, arrived);

                    agent.setCurrentNode(currentNode);
                    previousNode = currentNode;
                }

                agent.setStatus(AgentStatus.IDLE);
                pathStore.removePath(agent.getId());
                notifyAgentAvailable();
                log.info("에이전트 {}가 작업 및 복귀를 완료했습니다.", agent.getId());
            } catch (InterruptedException e) {
                log.error("에이전트 이동 시뮬레이션 중 예외 발생", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }


    private List<Node> calcAgentPathToTarget(Agent agent, int startTime, List<Constraint> constraints) {
        List<Node> pathToTarget = pathCalcService.calcPath(agent, constraints);
        if (pathToTarget == null) {
            agent.setStatus(AgentStatus.IDLE);
            throw new CustomException(ErrorCode.CANNOT_FIND_NEW_PATH);
        }
        pathStore.savePath(agent.getId(), startTime, pathToTarget);
        return pathToTarget;
    }

    private void reserveTaskLocationAndUpdateConstraints(Agent agent, int arrivalTime, List<Constraint> constraints) {
        Node taskNode = agent.getGoalNode();

        pathStore.reserveTaskLocation(agent.getId(), arrivalTime, taskNode, TASK_DURATION_TIME);

        for (int t = arrivalTime; t < arrivalTime + TASK_DURATION_TIME; t++) {
            constraints.add(Constraint.createVertexConstraint(agent.getId(), taskNode, t));
        }
    }

    private List<Node> calcAgentReturnPath(Agent agent, int startTime, List<Constraint> constraints) {
        agent.setCurrentNode(agent.getGoalNode());
        agent.setGoalNode(agent.getHomeNode());

        List<Node> returnPath = pathCalcService.calcPath(agent, constraints);
        if (returnPath == null) {
            agent.setStatus(AgentStatus.IDLE);
            throw new CustomException(ErrorCode.CANNOT_FIND_NEW_PATH);
        }
        pathStore.savePath(agent.getId(), startTime, returnPath);
        return returnPath;
    }

    private List<Node> assignFullPathToAgent(Agent agent, List<Node> pathToTarget, List<Node> returnPath) {
        List<Node> fullPath = new ArrayList<>(pathToTarget);

        for (int i = 0; i < TASK_DURATION_TIME; i++) {
            fullPath.add(agent.getGoalNode());
        }

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
    }

    private void sendAgentLocationUpdate(Agent agent, Node nextNode, int angle, boolean arrived) {
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
                    arrived
            );

            String jsonMessage = objectMapper.writeValueAsString(response);
            log.info("Unity 전송 데이터 : {}", jsonMessage);
            if (!webSocketSessionManager.sendMessageToPath(path, jsonMessage)) {
                log.error("메시지 전송 실패");
            }
        } catch (Exception e) {
            log.error("에이전트 위치 전송 실패", e);
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

    private boolean isRightTurn(Direction current, Direction next) {
        return (current == Direction.NORTH && next == Direction.EAST)
                || (current == Direction.EAST && next == Direction.SOUTH)
                || (current == Direction.SOUTH && next == Direction.WEST)
                || (current == Direction.WEST && next == Direction.NORTH);
    }

}
