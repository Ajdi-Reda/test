package com.codewithmosh.store.lab;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LabMapper {
    LabDto toDto(Lab lab);

    Lab toEntity(LabDto dto);
}
