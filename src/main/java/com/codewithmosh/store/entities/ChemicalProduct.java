package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "chemical_products")
public class ChemicalProduct {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "nomenclature")
    private String nomenclature;

    @Column(name = "current_stock")
    private Float currentStock;

    @Column(name = "minimum_stock")
    private Float minimumStock;

    @Column(name = "unit")
    private String unit;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Size(max = 30)
    @Column(name = "risk_category", length = 30)
    private String riskCategory;

    @Column(name = "safety_data_sheet_uri")
    private String safetyDataSheetUri;

    @OneToMany(mappedBy = "product")
    private Set<ChemicalUsage> chemicalUsages = new LinkedHashSet<>();

}