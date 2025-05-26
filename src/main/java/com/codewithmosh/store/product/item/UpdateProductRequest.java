package com.codewithmosh.store.product.item;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateProductRequest {
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @Size(max = 50, message = "Nomenclature must not exceed 50 characters")
    private String nomenclature;

    @PositiveOrZero(message = "Current stock must be zero or positive")
    private Float currentStock;

    @PositiveOrZero(message = "Minimum stock must be zero or positive")
    private Float minimumStock;

    @Pattern(regexp = "^[a-zA-Z]{1,10}$", message = "Unit must be 1-10 alphabetic characters")
    private String unit;

    @FutureOrPresent(message = "Expiration date must be in the present or future")
    private LocalDate expirationDate;

    @Pattern(regexp = "^(Low|Medium|High)$", message = "Risk category must be Low, Medium, or High")
    private String riskCategory;

    @Pattern(
            regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",
            message = "Safety data sheet URI must be a valid URL"
    )
    private String safetyDataSheetUri;
}
