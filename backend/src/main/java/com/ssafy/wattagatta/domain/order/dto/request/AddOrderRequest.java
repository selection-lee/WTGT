package com.ssafy.wattagatta.domain.order.dto.request;

public record AddOrderRequest(String invoiceNumber, String productName, int productQuantity) {
}
