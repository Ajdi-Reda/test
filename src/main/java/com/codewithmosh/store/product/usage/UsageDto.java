package com.codewithmosh.store.product.usage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class UsageDto {
        private Integer id;
        private Integer productId;
        private Float amount;
        private LocalDate date;
        private String purpose;
        private String status;
        private Integer handledBy;
        private Instant handledAt;
}
