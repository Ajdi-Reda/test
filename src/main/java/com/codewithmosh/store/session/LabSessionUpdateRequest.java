package com.codewithmosh.store.session;

import com.codewithmosh.store.equipments.CreateEquipmentLoanRequest;
import com.codewithmosh.store.product.usage.CreateUsageRequest;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class LabSessionUpdateRequest {

    // GroupId optional for update as well
    private Integer groupId;

    @NotNull(message = "Lab ID is required")
    private Integer labId;

    @NotNull(message = "Scheduled start is required")
    @FutureOrPresent(message = "Scheduled start must be in the present or future")
    private Instant scheduledStart;

    @NotNull(message = "Scheduled end is required")
    @FutureOrPresent(message = "Scheduled end must be in the present or future")
    private Instant scheduledEnd;

    // For update, createdBy usually doesn't change so it's optional
    private Integer createdBy;

    private Boolean validated;

    private List<CreateUsageRequest> productUsages;

    private List<CreateEquipmentLoanRequest> equipmentLoans;
}
