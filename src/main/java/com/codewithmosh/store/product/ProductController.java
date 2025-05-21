package com.codewithmosh.store.product;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public Iterable<ProductDto> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Integer id) {
        var product = productService.getProduct(id);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid CreateProductRequest request, UriComponentsBuilder uriBuilder) {
        var productDto = productService.createProduct(request);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id, @RequestBody @Valid UpdateProductRequest request) {
        var productDto = productService.updateProduct(id, request);
        if(productDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDto);
    }

    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleProductAlreadyExistsException(ProductAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", "product already exists"));
    }
}
