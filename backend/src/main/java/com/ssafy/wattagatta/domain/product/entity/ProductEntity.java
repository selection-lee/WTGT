package com.ssafy.wattagatta.domain.product.entity;

import com.ssafy.wattagatta.domain.invoice.entity.InvoiceEntity;
import com.ssafy.wattagatta.domain.order.dto.request.AddOrderRequest;
import com.ssafy.wattagatta.domain.product.dto.response.ProductResponse;
import com.ssafy.wattagatta.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "product_status", length = 10)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "product_size", length = 255)
    @Enumerated(EnumType.STRING)
    private ProductSize productSize;

    @Column(name = "product_location")
    private Integer productLocation;

    @Column(name = "expected_arrival_date")
    private LocalDate expectedArrivalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoiceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private AreaEntity areaEntity;


    public static ProductResponse toResponse(ProductEntity product) {
        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getQuantity(),
                product.getProductStatus(),
                product.getUnitPrice(),
                product.getTotalPrice(),
                product.getProductSize(),
                product.getProductLocation(),
                product.getCategoryEntity() != null ? product.getCategoryEntity().getCategoryName().name() : null,
                product.getInvoiceEntity() != null ? product.getInvoiceEntity().toString() : null,
                product.getAreaEntity().getAreaName().name(),
                product.getExpectedArrivalDate()
        );
    }

    public static ProductEntity from(AddOrderRequest request,
                                     CategoryEntity category,
                                     InvoiceEntity invoice,
                                     AreaEntity area){
        ProductEntity product = new ProductEntity();
        product.productName = request.productName();
        product.quantity = request.productQuantity();
        product.unitPrice = request.unitPrice();
        product.totalPrice = request.totalPrice();
        product.expectedArrivalDate = request.expectedArrivalDate();
        product.categoryEntity = category;
        product.invoiceEntity = invoice;
        product.areaEntity = area;
        product.productStatus = ProductStatus.PENDING_ARRIVAL;
        return product;
    }
}
