package com.codewithmosh.store.student_group;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentsGroupMapper {

    StudentsGroup toEntity(CreateStudentsGroupRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "group.id", target = "groupId")
    StudentsGroupDto toDto(StudentsGroup studentsGroup);

    void update(UpdateStudentsGroupRequest request, @MappingTarget StudentsGroup studentsGroup);
}
