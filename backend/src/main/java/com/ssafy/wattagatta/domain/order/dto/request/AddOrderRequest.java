package com.ssafy.wattagatta.domain.order.dto.request;

import com.ssafy.wattagatta.domain.product.entity.Category;
import java.time.LocalDate;

public record AddOrderRequest(String invoiceNumber,
                              String productName,
                              int productQuantity,
                              int unitPrice,
                              int totalPrice,
                              LocalDate expectedArrivalDate,
                              Category categoryName
) {
}
