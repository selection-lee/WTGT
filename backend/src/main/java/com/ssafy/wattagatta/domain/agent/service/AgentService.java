package com.ssafy.wattagatta.domain.agent.service;

import com.ssafy.wattagatta.domain.agent.manager.AgentManager;
import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.agent.model.Constraint;
import com.ssafy.wattagatta.domain.agent.model.Node;
import com.ssafy.wattagatta.domain.agent.utils.AStar;
import com.ssafy.wattagatta.domain.product.listener.ProductInfoListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AStar aStar;
    private final AgentManager agentManager;
    private final ProductInfoListener productInfoListener;

    public List<Node> calcPath(Agent agent, List<Constraint> constraints) {
        return aStar.findPath(agent, constraints);
    }

    private void findIdleAgent() {
        new Thread(() -> {
            while (true) {
                try {
                    Agent availableAgent = agentManager.waitForAvailableAgent();
                    agentManager.assignTaskToAgent(availableAgent, productInfoListener.getTargetLoc());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}
