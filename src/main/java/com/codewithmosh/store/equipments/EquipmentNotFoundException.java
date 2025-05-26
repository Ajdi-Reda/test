package com.codewithmosh.store.equipments;

public class EquipmentNotFoundException extends RuntimeException {
    public EquipmentNotFoundException() {
        super("Equipment not found");
    }
}
