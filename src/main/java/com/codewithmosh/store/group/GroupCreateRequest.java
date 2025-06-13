package com.codewithmosh.store.group;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCreateRequest {
    @NotNull
    private Integer subjectId;

    @NotNull
    private Integer teacherId;

    @NotNull
    @Size(max = 100)
    private String name;
}
