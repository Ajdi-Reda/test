package com.codewithmosh.store.equipments;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;


public interface EquipmentLoanRepository extends JpaRepository<Equipmentloan, Integer> {
    boolean existsByEquipmentIdAndReturnedFalse(Integer equipmentId);

    List<Equipmentloan> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT e FROM Equipmentloan e WHERE :time BETWEEN e.reservedFrom AND e.reservedTo AND (e.returned IS NULL OR e.returned = false)")
    List<Equipmentloan> findLoansAtTime(@Param("time") Instant time);

}