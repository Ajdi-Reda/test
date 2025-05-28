package com.codewithmosh.store.product.stock;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockAlertService {
    private StockAlertRepository stockAlertRepository;
    private StockAlertMapper stockAlertMapper;

    public Iterable<StockAlertDto> getStockAlerts() {
        return stockAlertRepository.findAll().stream().map(stockAlertMapper::toDto).collect(Collectors.toList());
    }

    public StockAlertDto createStockAlert(StockAlert stockAlert) {
        stockAlertRepository.save(stockAlert);
        return stockAlertMapper.toDto(stockAlert);
    }
}
