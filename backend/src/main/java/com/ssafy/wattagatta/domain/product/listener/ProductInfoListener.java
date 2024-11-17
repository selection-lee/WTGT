package com.ssafy.wattagatta.domain.product.listener;

import com.ssafy.wattagatta.domain.invoice.entity.InvoiceEntity;
import com.ssafy.wattagatta.domain.order.repository.InvoiceRepository;
import com.ssafy.wattagatta.domain.product.dto.ProductLoc;
import com.ssafy.wattagatta.domain.product.dto.request.ProductInfoMessage;
import com.ssafy.wattagatta.domain.product.entity.BaseSector;
import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import com.ssafy.wattagatta.domain.product.entity.ProductLogEntity;
import com.ssafy.wattagatta.domain.product.repository.ProductLogRepository;
import com.ssafy.wattagatta.domain.product.repository.ProductRepository;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductInfoListener {

    private final BlockingQueue<ProductLoc> queue = new LinkedBlockingQueue<>();
    private final ProductLogRepository productLogRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;

    public void receiveProductInfo(ProductInfoMessage productInfoMessage) {
        try {
            log.info("수신한 ProductInfo {}", productInfoMessage);
            productLogRepository.save(ProductLogEntity.fromInfo(productInfoMessage));

            InvoiceEntity invoice = invoiceRepository.findByInvoiceNumber(productInfoMessage.barNumber());
            if (invoice == null) {
                log.warn("해당 바코드({})로 Invoice 를 찾을 수 없습니다.", productInfoMessage.barNumber());
                return;
            }
            ProductEntity product = invoice.getProductEntity();
            product.moveToPendingTransport();
            productRepository.save(product);

            queue.put(new ProductLoc(invoice.getProductEntity().getProductId(), BaseSector.getLocationByRegionCode(productInfoMessage.regionCode())));
        } catch (Exception e) {
            log.error("작업할당 중 에러 발생", e);
        }
    }

    public ProductLoc getProductLoc() throws InterruptedException {
        ProductLoc productLoc = queue.take();
        ProductEntity product = productRepository.findById(productLoc.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.CANNOT_FIND_PRODUCT_ENTITY));
        product.changeTransit();
        productRepository.save(product);
        return productLoc;
    }


    public void requeueFailedTask(ProductLoc productLoc) {
        try {
            queue.put(productLoc);
            log.info("실패한 작업을 큐에 다시 추가 : {}", productLoc);
        } catch (InterruptedException e) {
            log.error("작업 재큐 중 에러 발생", e);
            Thread.currentThread().interrupt();
        }
    }
}
