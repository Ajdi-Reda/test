package com.codewithmosh.store.product.usage;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UsageRepository extends JpaRepository<ChemicalUsage, Integer> {
    List<ChemicalUsage> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
