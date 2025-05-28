package com.codewithmosh.store.product.stock;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products/stock-alerts")
@AllArgsConstructor
public class StockController {
    private final StockAlertService stockAlertService;
    @GetMapping
    public Iterable<StockAlertDto> getStockAlerts() {
        return stockAlertService.getStockAlerts();
    }
}
