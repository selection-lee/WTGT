package com.ssafy.wattagatta.domain.product.dto.request;

public record ProductInfoMessage(
        String senderName,
        String recipientName,
        String senderAddress,
        String recipientAddress,
        String productName,
        int productQuantity,
        int unitPrice,
        int totalPrice,
        String barNumber,
        String regionCode) {
}
