package com.codewithmosh.store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ProductDto {
    private Integer id;
    private String name;
    private String nomenclature;
    private Float currentStock;
    private Float minimumStock;
    private String unit;
    private LocalDate expirationDate;
    private String riskCategory;
    private String safetyDataSheetUri;
}

