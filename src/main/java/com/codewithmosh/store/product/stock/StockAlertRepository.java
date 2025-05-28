package com.codewithmosh.store.product.stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockAlertRepository extends JpaRepository<StockAlert, Integer> {
    Boolean existsByItemIdAndResolvedFalse(Integer id);
}
