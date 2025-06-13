package com.codewithmosh.store.group;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupDto {
    private Integer id;
    private Integer subjectId;
    private String name;
    private Integer teacherId;
}
