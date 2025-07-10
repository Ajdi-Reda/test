package com.codewithmosh.store.equipments;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface EquipmentLoanRepository extends JpaRepository<Equipmentloan, Integer> {
    boolean existsByEquipmentIdAndReturnedFalse(Integer equipmentId);

    List<Equipmentloan> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}