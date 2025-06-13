package com.codewithmosh.store.session;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class LabSessionCreateRequest {

    // GroupId is now optional, so no @NotNull here
    private Integer groupId;

    @NotNull(message = "Lab ID is required")
    private Integer labId;

    @NotNull(message = "Scheduled start is required")
    @FutureOrPresent(message = "Scheduled start must be in the present or future")
    private Instant scheduledStart;

    @NotNull(message = "Scheduled end is required")
    @FutureOrPresent(message = "Scheduled end must be in the present or future")
    private Instant scheduledEnd;

    // createdBy is required to know who creates the session
    @NotNull(message = "CreatedBy (user ID) is required")
    private Integer createdBy;

    private Boolean validated = false;
}
