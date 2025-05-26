package com.codewithmosh.store.equipments;

import lombok.Data;

import java.time.Instant;

@Data
public class UpdateEquipmentLoanRequest {
    private Instant reservedTo;
    private Boolean returned;
    private Instant actualReturnDate;
    private Integer returnedToId;
}

