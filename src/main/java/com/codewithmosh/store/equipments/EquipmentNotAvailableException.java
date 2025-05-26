package com.codewithmosh.store.equipments;

public class EquipmentNotAvailableException extends RuntimeException {
  public EquipmentNotAvailableException() {
    super("Equipment is not available for loan");
  }
}
