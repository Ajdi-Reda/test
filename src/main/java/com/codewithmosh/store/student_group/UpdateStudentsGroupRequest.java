package com.codewithmosh.store.student_group;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStudentsGroupRequest {
    @NotNull
    private Integer groupId;

    @NotNull
    private Integer userId;
}
