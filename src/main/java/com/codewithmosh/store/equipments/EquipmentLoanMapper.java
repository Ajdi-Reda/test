package com.codewithmosh.store.equipments;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EquipmentLoanMapper {
    @Mapping(target = "equipmentId", source = "equipment.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "checkoutById", source = "checkoutBy.id")
    @Mapping(target = "returnedToId", source = "returnedTo.id")
    EquipmentLoanDto toDto(Equipmentloan loan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipment.id", source = "equipmentId")
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "checkoutBy.id", source = "checkoutById")
    @Mapping(target = "returned", constant = "false")
    @Mapping(target = "actualReturnDate", ignore = true)
    @Mapping(target = "returnedTo", ignore = true)
    Equipmentloan toEntity(CreateEquipmentLoanRequest request);

    void update(UpdateEquipmentLoanRequest request, @MappingTarget Equipmentloan loan);
}