package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.ChemicalProduct;
import com.codewithmosh.store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ChemicalProduct, Integer> {
    Boolean existsByNomenclature(String nomenclature);
}