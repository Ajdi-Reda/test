package com.codewithmosh.store.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class CreateProductRequest {
    @Positive(message = "ID must be a positive number")
    private Integer id;

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Nomenclature is required")
    @Size(max = 50, message = "Nomenclature must not exceed 50 characters")
    private String nomenclature;

    @NotNull(message = "Current stock is required")
    @PositiveOrZero(message = "Current stock must be zero or positive")
    private Float currentStock;

    @NotNull(message = "Minimum stock is required")
    @PositiveOrZero(message = "Minimum stock must be zero or positive")
    private Float minimumStock;

    @NotBlank(message = "Unit is required")
    @Pattern(regexp = "^[a-zA-Z]{1,10}$", message = "Unit must be 1-10 alphabetic characters")
    private String unit;

    @FutureOrPresent(message = "Expiration date must be in the present or future")
    private LocalDate expirationDate;

    @NotBlank(message = "Risk category is required")
    @Pattern(regexp = "^(Low|Medium|High)$", message = "Risk category must be Low, Medium, or High")
    private String riskCategory;

    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",
            message = "Safety data sheet URI must be a valid URL")
    private String safetyDataSheetUri;
}