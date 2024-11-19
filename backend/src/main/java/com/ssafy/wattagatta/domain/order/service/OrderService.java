package com.ssafy.wattagatta.domain.order.service;

import com.ssafy.wattagatta.domain.invoice.entity.InvoiceEntity;
import com.ssafy.wattagatta.domain.invoice.entity.RecipientEntity;
import com.ssafy.wattagatta.domain.invoice.entity.SenderEntity;
import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
import com.ssafy.wattagatta.domain.member.repository.MemberRepository;
import com.ssafy.wattagatta.domain.order.dto.request.AddOrderRequest;
import com.ssafy.wattagatta.domain.order.entity.OrderEntity;
import com.ssafy.wattagatta.domain.order.repository.AreaRepository;
import com.ssafy.wattagatta.domain.order.repository.CategoryRepository;
import com.ssafy.wattagatta.domain.order.repository.InvoiceRepository;
import com.ssafy.wattagatta.domain.order.repository.OrderRepository;
import com.ssafy.wattagatta.domain.order.repository.RecipientRepository;
import com.ssafy.wattagatta.domain.order.repository.SenderRepository;
import com.ssafy.wattagatta.domain.product.entity.AreaEntity;
import com.ssafy.wattagatta.domain.product.entity.AreaName;
import com.ssafy.wattagatta.domain.product.entity.CategoryEntity;
import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import com.ssafy.wattagatta.domain.product.repository.ProductRepository;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AreaRepository areaRepository;
    private final InvoiceRepository invoiceRepository;
    private final SenderRepository senderRepository;
    private final RecipientRepository recipientRepository;

    public void addOrder(AddOrderRequest request) {
        MemberEntity member = memberRepository.findById(1)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        CategoryEntity category = categoryRepository.findByCategoryName(request.categoryName());
        AreaEntity area = areaRepository.findByAreaName(AreaName.GWANGJU);

        SenderEntity sender = SenderEntity.defaultCreate();
        senderRepository.save(sender);

        RecipientEntity recipient = RecipientEntity.defaultCreate();
        recipientRepository.save(recipient);

        InvoiceEntity invoice = InvoiceEntity.createInvoice(request.invoiceNumber(), sender, recipient,
                request.totalPrice());
        invoiceRepository.save(invoice);

        orderRepository.save(OrderEntity.from(request, member, invoice));

        ProductEntity product = ProductEntity.from(request, category, invoice, area);
        productRepository.save(product);
    }
}
