package com.ssafy.wattagatta.domain.product.listener;

import com.ssafy.wattagatta.domain.product.dto.TargetLoc;
import com.ssafy.wattagatta.domain.product.dto.request.ProductInfoMessage;
import com.ssafy.wattagatta.domain.product.entity.BaseSector;
import com.ssafy.wattagatta.domain.product.entity.ProductLogEntity;
import com.ssafy.wattagatta.domain.product.repository.ProductLogRepository;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductInfoListener {

    private final BlockingQueue<TargetLoc> queue = new LinkedBlockingQueue<>();
    private final ProductLogRepository productLogRepository;

    public void receiveProductInfo(ProductInfoMessage productInfoMessage) {
        try {
            log.info("수신한 ProductInfo {}", productInfoMessage);
            productLogRepository.save(ProductLogEntity.fromInfo(productInfoMessage));
            queue.add(BaseSector.getLocationByRegionCode(productInfoMessage.regionCode()));
        } catch (Exception e) {
            log.error("작업할당 중 에러 발생", e);
        }
    }

    public TargetLoc getTargetLoc() throws InterruptedException {
        return queue.take();
    }
}
