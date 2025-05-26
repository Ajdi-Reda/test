package com.codewithmosh.store.equipments;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EquipmentDto {
    private Integer id;
    private String barcode;
    private String brand;
    private String model;
    private LocalDate purchaseDate;
    private String status;
}
