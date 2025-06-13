package com.codewithmosh.store.product.usage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsageMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "takenBy.id", target = "takenBy")
    @Mapping(source = "handledBy.id", target = "handledBy")
    @Mapping(source = "session.id", target = "sessionId")
    UsageDto toDto(ChemicalUsage usage);

    @Mapping(target = "takenBy", ignore = true)
    ChemicalUsage toEntity(CreateUsageRequest request);

    @Mapping(target = "takenBy", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "handledBy", ignore = true)
    @Mapping(target = "session", ignore = true)
    void update(UpdateUsageRequest request, @MappingTarget ChemicalUsage usage);
}
