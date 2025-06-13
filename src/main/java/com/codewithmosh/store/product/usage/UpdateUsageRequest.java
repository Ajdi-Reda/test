package com.codewithmosh.store.product.usage;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class UpdateUsageRequest {

    private Integer sessionId;

    private Integer productId;

    private Integer takenBy;

    @Positive(message = "Amount must be positive")
    private Float amount;

    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    private String purpose;

    private String status;

    private Integer handledBy;

    private Instant handledAt;
}
