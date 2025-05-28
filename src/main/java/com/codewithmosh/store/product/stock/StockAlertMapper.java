package com.codewithmosh.store.product.stock;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockAlertMapper {

    StockAlertDto toDto(StockAlert alert);
//    StockAlert toEntity(CreateUsageRequest request);
}
