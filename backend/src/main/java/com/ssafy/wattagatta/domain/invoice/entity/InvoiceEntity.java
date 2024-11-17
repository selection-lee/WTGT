package com.ssafy.wattagatta.domain.invoice.entity;

import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "shipping_fee")
    private Integer shippingFee;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "tax")
    private Integer tax;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private SenderEntity senderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private RecipientEntity recipientEntity;

    @OneToOne(mappedBy = "invoiceEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProductEntity productEntity;


    public static InvoiceEntity createInvoice(String invoiceNumber,
                                              SenderEntity sender,
                                              RecipientEntity recipient,
                                              int totalPrice) {
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.invoiceNumber = invoiceNumber;
        invoice.issueDate = LocalDateTime.now();
        invoice.dueDate = LocalDateTime.now().plusDays(7);
        invoice.shippingFee = 5000;
        invoice.tax = 1000;
        invoice.totalAmount = totalPrice + 5000 + 1000;
        invoice.senderEntity = sender;
        invoice.recipientEntity = recipient;
        return invoice;
    }

    public void moveProductToPendingTransport() {
        if (productEntity == null) {
            throw new CustomException(ErrorCode.CANNOT_FIND_PRODUCT_ENTITY);
        }

        if (productEntity.canMoveToPendingTransport()) {
            productEntity.moveToPendingTransport();
        } else {
            throw new CustomException(ErrorCode.CANNOT_CHANGE_PRODUCT_STATUS);
        }
    }
}
