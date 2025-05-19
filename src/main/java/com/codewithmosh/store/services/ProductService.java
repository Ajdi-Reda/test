package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.requests.CreateProductRequest;
import com.codewithmosh.store.dtos.requests.UpdateProductRequest;
import com.codewithmosh.store.dtos.response.ProductDto;
import com.codewithmosh.store.entities.ChemicalProduct;
import com.codewithmosh.store.exceptions.ProductAlreadyExistsException;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Iterable<ProductDto> getProducts() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    public ProductDto getProduct(Integer id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }

        return productMapper.toDto(product);
    }

    public ProductDto createProduct(CreateProductRequest request) {
        if(productRepository.existsByNomenclature(request.getNomenclature())) {
            throw new ProductAlreadyExistsException();
        }

        ChemicalProduct product = productMapper.toEntity(request);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Integer id, UpdateProductRequest request) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }

        productMapper.update(request, product);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public void deleteProduct(Integer id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        productRepository.deleteById(id);
    }
}
