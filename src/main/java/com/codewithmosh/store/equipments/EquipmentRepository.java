package com.codewithmosh.store.equipments;

import com.codewithmosh.store.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;



public interface EquipmentRepository extends JpaRepository<Equipment, Integer>{
    List<Equipment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Boolean existsByBarcode(String barcode);
}
