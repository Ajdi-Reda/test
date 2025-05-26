package com.codewithmosh.store.equipments;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEquipmentRequest {

    @Size(min = 3, max = 100, message = "Barcode must be between 3 and 100 characters")
    private String barcode;

    @Size(min = 2, max = 50, message = "Brand must be between 2 and 50 characters")
    private String brand;

    @Size(min = 1, max = 100, message = "Model must be between 1 and 100 characters")
    private String model;

    @PastOrPresent(message = "Purchase date must be in the past or present")
    private LocalDate purchaseDate;

    @Pattern(regexp = "^(New|Used|Damaged|Retired)$", message = "Status must be New, Used, Damaged, or Retired")
    private String status;
}
