package com.codewithmosh.store.equipments;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateEquipmentRequest {

    @Positive(message = "ID must be a positive number")
    private Integer id;

    @NotBlank(message = "Barcode is required")
    @Size(min = 3, max = 100, message = "Barcode must be between 3 and 100 characters")
    private String barcode;

    @NotBlank(message = "Brand is required")
    @Size(min = 2, max = 50, message = "Brand must be between 2 and 50 characters")
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(min = 1, max = 100, message = "Model must be between 1 and 100 characters")
    private String model;

    @NotNull(message = "Purchase date is required")
    @PastOrPresent(message = "Purchase date must be in the past or present")
    private LocalDate purchaseDate;

    @Pattern(regexp = "^(NEW|USED|DAMAGED|RETIRED)$", message = "Status must be NEW, USED, DAMAGED, or RETIRED")
    private String status;
}
