package com.codewithmosh.store.group;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    // Ignore these fields here, because you will set them manually in the service
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    Group toEntity(GroupCreateRequest request);

    @Mapping(target = "subjectId", source = "subject.id")
    @Mapping(target = "teacherId", source = "teacher.id")
    GroupDto toDto(Group group);

    // Ignore these fields in update as well
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    void update(GroupCreateRequest request, @MappingTarget Group group);
}

