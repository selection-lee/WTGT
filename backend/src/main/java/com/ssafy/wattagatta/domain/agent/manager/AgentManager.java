package com.ssafy.wattagatta.domain.agent.manager;

import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.agent.model.Direction;
import com.ssafy.wattagatta.domain.agent.model.Node;
import com.ssafy.wattagatta.domain.product.dto.TargetLoc;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AgentManager {

    private final List<Agent> agents = new ArrayList<>();

    private Agent agent1;
    private Agent agent2;
    private final Node agent1Home = new Node(1, 0, Direction.EAST);
    private final Node agent2Home = new Node(0, 1, Direction.EAST);

    public AgentManager() {
        agent1.ready("agent1", agent1Home);
        agent2.ready("agent2", agent2Home);
        agents.add(agent1);
        agents.add(agent2);
    }

    public void assignTaskToAgent(Agent agent, TargetLoc targetLoc) {

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
