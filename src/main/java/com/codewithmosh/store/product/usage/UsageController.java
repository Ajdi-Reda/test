package com.codewithmosh.store.product.usage;

import com.codewithmosh.store.product.item.ProductNotFoundExceptionException;
import com.codewithmosh.store.user.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/products/usages")
@AllArgsConstructor
public class UsageController {
    private final UsageService usageService;
    @GetMapping
    public Iterable<UsageDto> usage() {
        return usageService.getUsages();
    }

    @PostMapping
    public ResponseEntity<UsageDto> create(@RequestBody @Valid CreateUsageRequest request, UriComponentsBuilder uriBuilder) {
        var usageSDto = usageService.createUsage(request);
        var uri = uriBuilder.path("/products/usage/{id}").buildAndExpand(usageSDto.getId()).toUri();

        return ResponseEntity.created(uri).body(usageSDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsageDto> update(@PathVariable Integer id, @RequestBody @Valid UpdateUsageRequest request) {
        var usageDto = usageService.updateUsage(id, request);
        return ResponseEntity.ok(usageDto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
    }

    @ExceptionHandler(ProductNotFoundExceptionException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(ProductNotFoundExceptionException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", "product not found"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
