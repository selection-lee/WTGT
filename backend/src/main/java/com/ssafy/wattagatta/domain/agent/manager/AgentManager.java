package com.ssafy.wattagatta.domain.agent.manager;

import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.agent.model.AgentStatus;
import com.ssafy.wattagatta.domain.agent.model.Constraint;
import com.ssafy.wattagatta.domain.agent.model.Direction;
import com.ssafy.wattagatta.domain.agent.model.Node;
import com.ssafy.wattagatta.domain.agent.service.AgentService;
import com.ssafy.wattagatta.domain.agent.utils.PathStore;
import com.ssafy.wattagatta.domain.product.dto.TargetLoc;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import com.ssafy.wattagatta.global.utils.GlobalClock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AgentManager {

    private final List<Agent> agents;
    private final PathStore pathStore;
    private final GlobalClock globalClock;
    private final AgentService agentService;

    private Agent agent1 = new Agent();
    private Agent agent2 = new Agent();

    private final int TASK_DURATION_TIME = 5;

    public AgentManager(AgentService agentService, GlobalClock globalClock) {
        this.globalClock = globalClock;
        this.agentService = agentService;
        this.agents = new ArrayList<>();
        this.pathStore = new PathStore();
        agent1.ready("agent1", new Node(1, 0, Direction.EAST));
        agent2.ready("agent2", new Node(0, 1, Direction.EAST));
        agents.add(agent1);
        agents.add(agent2);
    }

    public void assignTaskToAgent(Agent agent, TargetLoc targetLoc) {
        int currentGlobalTime = globalClock.getGlobalTime();
        agent.assignTask(targetLoc, currentGlobalTime);

        Map<String, Map<Integer, Node>> allAgentPositions = pathStore.getAllPositions();
        List<Constraint> pathConstraints = pathStore.getConstraintsForAgent(agent.getId());

        List<Node> newPath = agentService.calcPath(agent, pathConstraints);
        if (newPath == null) {
            agent.setStatus(AgentStatus.IDLE);
            throw new CustomException(ErrorCode.CANNOT_FIND_NEW_PATH);
        }

        agent.setCurrentPath(new ArrayList<>(newPath));
        log.info("에이전트 {}의 경로: {}", agent.getId(), newPath);

        pathStore.savePath(agent.getId(), currentGlobalTime, newPath);

        pathStore.reserveTaskLocation(agent.getId(), currentGlobalTime + newPath.size(),
                newPath.get(newPath.size() - 1), TASK_DURATION_TIME);
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

}
