package com.ssafy.wattagatta.domain.agent.service;

import com.ssafy.wattagatta.domain.agent.manager.AgentManager;
import com.ssafy.wattagatta.domain.agent.model.Agent;
import com.ssafy.wattagatta.domain.product.dto.ProductLoc;
import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import com.ssafy.wattagatta.domain.product.listener.ProductInfoListener;
import com.ssafy.wattagatta.domain.product.repository.ProductRepository;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentManager agentManager;
    private final ProductInfoListener productInfoListener;
    private final ProductRepository productRepository;

    @PostConstruct
    private void startFindIdleAgent() {
        findIdleAgent();
    }

    private void findIdleAgent() {
        new Thread(() -> {
            while (true) {
                try {
                    ProductLoc productLoc = productInfoListener.getProductLoc();
                    Agent availableAgent = agentManager.waitForAvailableAgent();
                    agentManager.assignTaskToAgent(
                            availableAgent,
                            productLoc,
                            productInfoListener::requeueFailedTask
                    );
//                    // 적재 완료
//                    ProductEntity product = productRepository.findById(productLoc.productId())
//                            .orElseThrow(() -> new CustomException(ErrorCode.CANNOT_FIND_PRODUCT_ENTITY));
//                    product.changeLoaded();
//                    productRepository.save(product);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}
