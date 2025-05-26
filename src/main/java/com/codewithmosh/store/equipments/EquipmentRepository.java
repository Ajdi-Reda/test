package com.codewithmosh.store.equipments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface EquipmentRepository extends JpaRepository<Equipment, Integer>{
    Boolean existsByBarcode(String barcode);
}
