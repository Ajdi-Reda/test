package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.requests.CreateProductRequest;
import com.codewithmosh.store.dtos.requests.UpdateProductRequest;
import com.codewithmosh.store.dtos.response.ProductDto;
import com.codewithmosh.store.entities.ChemicalProduct;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(ChemicalProduct product);
    ChemicalProduct toEntity(CreateProductRequest request);
    void update(UpdateProductRequest request, @MappingTarget ChemicalProduct product);
}

