package com.codewithmosh.store.equipments;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
public class EquipmentLoanDto {
    private Integer id;
    private Integer equipmentId;
    private Integer userId;
    private Instant reservedFrom;
    private Instant reservedTo;
    private String signature;
    private Boolean returned;
    private Instant expectedReturnDate;
    private Instant actualReturnDate;
    private Integer checkoutById;
    private Integer returnedToId;
    private Integer sessionId;
}
