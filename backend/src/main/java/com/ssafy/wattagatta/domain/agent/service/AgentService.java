package com.ssafy.wattagatta.domain.agent.service;

import com.ssafy.wattagatta.domain.agent.manager.AgentManager;
import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.product.listener.ProductInfoListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentManager agentManager;
    private final ProductInfoListener productInfoListener;

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
