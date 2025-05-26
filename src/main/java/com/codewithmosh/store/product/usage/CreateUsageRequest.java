package com.codewithmosh.store.product.usage;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUsageRequest {

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "User ID (takenBy) is required")
    private Integer takenBy;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Float amount;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @NotBlank(message = "Purpose is required")
    private String purpose;
}
