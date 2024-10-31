package com.ssafy.wattagatta.domain.invoice.entity;

import com.ssafy.wattagatta.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
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

    @OneToMany(mappedBy = "recipientEntity", fetch = FetchType.LAZY)
    private Set<InvoiceEntity> invoiceEntities;
}
