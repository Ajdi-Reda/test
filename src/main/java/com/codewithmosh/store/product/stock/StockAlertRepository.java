package com.codewithmosh.store.product.stock;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StockAlertRepository extends JpaRepository<StockAlert, Integer> {
    Boolean existsByItemIdAndResolvedFalse(Integer id);
    List<StockAlert> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
