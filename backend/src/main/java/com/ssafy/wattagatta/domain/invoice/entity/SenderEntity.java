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
@Table(name = "sender")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SenderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "address", length = 100)
    private String address;

    @OneToOne(mappedBy = "senderEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private InvoiceEntity invoiceEntity;

    public static SenderEntity defaultCreate() {
        SenderEntity sender = new SenderEntity();
        sender.name = "samsung electronics";
        sender.email = "samsung@ssafy.com";
        sender.phone = "010-1234-5678";
        sender.address = "삼성전자 사업장";
        return sender;
    }
}
