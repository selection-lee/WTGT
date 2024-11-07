package com.ssafy.wattagatta.domain.product.dto.response;

import com.ssafy.wattagatta.domain.product.entity.ProductSize;
import com.ssafy.wattagatta.domain.product.entity.ProductStatus;
import java.time.LocalDate;

public record ProductResponse(
        Integer productId,
        String productName,
        Integer quantity,
        ProductStatus productStatus,
        Integer unitPrice,
        Integer totalPrice,
        ProductSize productSize,
        Integer productLocation,
        String categoryName,
        String invoiceDetails,
        String areaName,
        LocalDate expectedArrivalDate
) {
}
