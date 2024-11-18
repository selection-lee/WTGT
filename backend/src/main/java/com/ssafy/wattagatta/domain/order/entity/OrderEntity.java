package com.ssafy.wattagatta.domain.order.entity;

import com.ssafy.wattagatta.domain.invoice.entity.InvoiceEntity;
import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
import com.ssafy.wattagatta.domain.order.dto.request.AddOrderRequest;
import com.ssafy.wattagatta.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "product_name", length = 100)
    private String productName;

    @Column(name = "invoice_number", length = 30)
    private String invoiceNumber;

    @Column(name = "product_quantity")
    private Integer productQuantity;

    @Column(name = "is_store", nullable = false)
    private Boolean isStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @OneToOne
    @JoinColumn(name = "invoice_id", unique = true)
    private InvoiceEntity invoiceEntity;

    @Builder
    public OrderEntity(String productName, String invoiceNumber, Integer productQuantity, Boolean isStore,
                       MemberEntity memberEntity, InvoiceEntity invoiceEntity) {
        this.productName = productName;
        this.invoiceNumber = invoiceNumber;
        this.productQuantity = productQuantity;
        this.isStore = isStore;
        this.memberEntity = memberEntity;
        this.invoiceEntity = invoiceEntity;
    }

    public static OrderEntity from(AddOrderRequest request, MemberEntity memberEntity, InvoiceEntity invoiceEntity) {
        return OrderEntity.builder()
                .invoiceNumber(request.invoiceNumber())
                .productName(request.productName())
                .productQuantity(request.productQuantity())
                .memberEntity(memberEntity)
                .invoiceEntity(invoiceEntity)
                .isStore(false)
                .build();
    }

}
