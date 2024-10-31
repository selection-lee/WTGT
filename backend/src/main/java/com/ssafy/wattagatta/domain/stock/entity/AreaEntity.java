package com.ssafy.wattagatta.domain.stock.entity;

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
@Table(name = "area")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AreaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "area_name", length = 100, nullable = false)
    private String areaName;

    @Column(name = "base_sector", length = 100, nullable = false)
    private String baseSector;

    @OneToMany(mappedBy = "areaEntity", fetch = FetchType.LAZY)
    private Set<SectorEntity> sectorEntities;

}
