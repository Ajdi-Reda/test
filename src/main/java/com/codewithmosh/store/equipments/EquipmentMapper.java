package com.codewithmosh.store.equipments;



import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
    EquipmentDto toDto(Equipment equipment);
    Equipment toEntity(CreateEquipmentRequest equipmentDto);
    void update(UpdateEquipmentRequest request, @MappingTarget Equipment equipment);
}
