package com.codewithmosh.store.equipments;

public class EquipmentLoanNotFoundException extends RuntimeException {
  public EquipmentLoanNotFoundException() {
    super("Equipment loan not found");
  }
}

