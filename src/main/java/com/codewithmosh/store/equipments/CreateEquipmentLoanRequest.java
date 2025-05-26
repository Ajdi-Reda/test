package com.codewithmosh.store.equipments;

import lombok.Data;
import java.time.Instant;

@Data
public class CreateEquipmentLoanRequest {
    private Integer equipmentId;
    private Integer userId;
    private Instant reservedFrom;
    private Instant reservedTo;
    private String signature;
    private Instant expectedReturnDate;
    private Integer checkoutById;
}

