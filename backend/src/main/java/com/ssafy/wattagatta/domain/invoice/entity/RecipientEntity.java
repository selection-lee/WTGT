package com.ssafy.wattagatta.domain.invoice.entity;

import com.ssafy.wattagatta.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "recipient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipientEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipient_id")
    private Integer recipientId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "address", length = 100)
    private String address;

    @OneToOne(mappedBy = "recipientEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private InvoiceEntity invoiceEntity;

    public static RecipientEntity defaultCreate() {
        RecipientEntity recipient = new RecipientEntity();
        recipient.name = "SSAFY";
        recipient.email = "SSAFY@ssafy.com";
        recipient.phone = "010-5161-5432";
        recipient.address = "삼성전자 광주 사업장 그린캠퍼스";
        return recipient;
    }
}
