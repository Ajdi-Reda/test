package com.codewithmosh.store.subject;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectDto toDto(Subject subject);
    Subject toEntity(SubjectDto dto);
}
