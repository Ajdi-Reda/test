package com.codewithmosh.store.product.item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ChemicalProduct, Integer> {
    Boolean existsByNomenclature(String nomenclature);
}