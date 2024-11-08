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
import java.util.Map;
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

    public void assignTaskToAgent(Agent agent, TargetLoc targetLoc) {
        int currentGlobalTime = globalClock.getGlobalTime();
        agent.assignTask(targetLoc, currentGlobalTime);

        Map<String, Map<Integer, Node>> allAgentPositions = pathStore.getAllPositions();
        List<Node> newPath = calcAgentPath(agent);
        log.info("에이전트 {}의 경로: {}", agent.getId(), newPath);

        pathStore.savePath(agent.getId(), currentGlobalTime, newPath);
        pathStore.reserveTaskLocation(agent.getId(), currentGlobalTime + newPath.size(),
                newPath.get(newPath.size() - 1), TASK_DURATION_TIME);

        int finalCurrentGlobalTime = currentGlobalTime + newPath.size() + TASK_DURATION_TIME;
        agent.moveAlongPath(
                // agent 이동
                nextNode -> {
                    int angle = calculateAngle(agent.getCurrentNode(), nextNode);
                    boolean arrived = agent.getCurrentPath().isEmpty();
                    sendAgentLocationUpdate(agent, nextNode, angle, arrived);
                },
                // agent 작업 수행
                () -> {
                    agent.performTask(TASK_DURATION_TIME);
                    agent.assignReturnHomeTask(finalCurrentGlobalTime);
                    List<Node> homePath = calcAgentPath(agent);
                    pathStore.savePath(agent.getId(), globalClock.getGlobalTime(), homePath);
                    pathStore.reserveTaskLocation(agent.getId(), currentGlobalTime + homePath.size(),
                            homePath.get(homePath.size() - 1), TASK_DURATION_TIME);
                },
                // agent 복귀 준비
                () -> {
                    log.info("에이전트 {}의 복귀 경로: {}", agent.getId(), agent1.getCurrentPath());
                    agent.setCurrentNode(agent.getGoalNode());
                    agent.setGoalNode(agent.getHomeNode());
                    agent.setStatus(AgentStatus.RETURNING_HOME);
                },
                nextNode -> {
                    int angle = calculateAngle(agent.getCurrentNode(), nextNode);
                    boolean arrived = agent.getCurrentPath().isEmpty();
                    sendAgentLocationUpdate(agent, nextNode, angle, arrived);
                },
                // 복귀 완료
                () -> {
                    if (agent.getStatus() == AgentStatus.RETURNING_HOME) {
                        agent.setStatus(AgentStatus.IDLE);
                        pathStore.removePath(agent.getId());
                        notifyAgentAvailable();
                    }
                }
        );
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
