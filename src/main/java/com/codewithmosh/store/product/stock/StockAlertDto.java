package com.codewithmosh.store.product.stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class StockAlertDto {
    private Integer id;
    private Integer itemId;
    private LocalDate date;
    private boolean resolved;
    private Integer resolvedById;
}
