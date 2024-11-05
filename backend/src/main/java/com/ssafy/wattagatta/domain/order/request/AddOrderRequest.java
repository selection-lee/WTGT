package com.ssafy.wattagatta.domain.order.request;

public record AddOrderRequest(String invoiceNumber, String productName, int productQuantity) {
}
