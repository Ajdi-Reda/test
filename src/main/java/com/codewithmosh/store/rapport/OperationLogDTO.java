package com.codewithmosh.store.rapport;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLogDTO {
    private String type;
    private String description;
    private LocalDateTime timestamp;
    private String performedBy;
}
