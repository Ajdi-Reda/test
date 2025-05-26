package com.codewithmosh.store.product.usage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageRepository extends JpaRepository<ChemicalUsage, Integer> {
}
