package com.codewithmosh.store.product.item;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface ProductRepository extends JpaRepository<ChemicalProduct, Integer> {
    Boolean existsByNomenclature(String nomenclature);
    List<ChemicalProduct> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}