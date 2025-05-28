package com.codewithmosh.store.equipments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentLoanRepository extends JpaRepository<Equipmentloan, Integer> {
    boolean existsByEquipmentIdAndReturnedFalse(Integer equipmentId);
}