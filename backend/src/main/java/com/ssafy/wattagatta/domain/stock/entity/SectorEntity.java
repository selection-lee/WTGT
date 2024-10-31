package com.ssafy.wattagatta.domain.stock.entity;

import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import com.ssafy.wattagatta.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sector")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SectorEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sector_id")
    private Integer sectorId;

    @Column(name = "sector_number", length = 100, nullable = false)
    private String sectorNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private AreaEntity areaEntity;

    @OneToMany(mappedBy = "sectorEntity", fetch = FetchType.LAZY)
    private Set<ProductEntity> productEntities;

}
