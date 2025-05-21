package com.codewithmosh.store.product;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(ChemicalProduct product);
    ChemicalProduct toEntity(CreateProductRequest request);
    void update(UpdateProductRequest request, @MappingTarget ChemicalProduct product);
}

