package com.codewithmosh.store.product.usage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UsageMapper {

    @Mappings({
            @Mapping(source = "product.id", target = "productId"),
            @Mapping(source = "user.id", target = "takenBy"),
            @Mapping(source = "handledBy.id", target = "handledBy"),
    })
    UsageDto toDto(ChemicalUsage usage);
    ChemicalUsage toEntity(CreateUsageRequest request);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "handledBy", ignore = true)
    void update(UpdateUsageRequest request, @MappingTarget ChemicalUsage usage);
}
