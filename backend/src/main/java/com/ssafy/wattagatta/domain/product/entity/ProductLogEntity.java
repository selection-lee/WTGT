package com.ssafy.wattagatta.domain.product.entity;

import com.ssafy.wattagatta.domain.product.dto.request.ProductInfoMessage;
import com.ssafy.wattagatta.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "inbound_product_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLogEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inbound_id")
    private Integer inboundId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "sender_address")
    private String senderAddress;

    @Column(name = "recipient_address")
    private String recipientAddress;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_quantity")
    private Integer productQuantity;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "bar_number")
    private String barNumber;

    @Column(name = "region_code")
    private String regionCode;

    public static ProductLogEntity fromInfo(ProductInfoMessage info) {
        ProductLogEntity entity = new ProductLogEntity();
        entity.senderName = info.senderName();
        entity.recipientName = info.recipientName();
        entity.senderAddress = info.senderAddress();
        entity.recipientAddress = info.recipientAddress();
        entity.productName = info.productName();
        entity.productQuantity = info.productQuantity();
        entity.unitPrice = info.unitPrice();
        entity.totalPrice = info.totalPrice();
        entity.barNumber = info.barNumber();
        entity.regionCode = info.regionCode();
        return entity;
    }
}
