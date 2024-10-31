package com.ssafy.wattagatta.domain.invoice.entity;

import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
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

}
