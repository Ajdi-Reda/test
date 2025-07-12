package com.codewithmosh.store.session;

import com.codewithmosh.store.equipments.EquipmentLoanMapper;
import com.codewithmosh.store.product.usage.UsageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EquipmentLoanMapper.class, UsageMapper.class})public interface LabSessionMapper {

    // For create, ignore group, lab, createdBy entities - set them manually in service
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "lab", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    LabSession toEntity(LabSessionCreateRequest request);

    // For DTO, map nested objects to their IDs
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(source = "lab.id", target = "labId")
    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "createdBy.name", target = "createdByName")
    LabSessionDto toDto(LabSession labSession);

    // Update entity, ignoring group, lab, createdBy (handle manually)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "lab", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void update(LabSessionUpdateRequest request, @MappingTarget LabSession labSession);
}
