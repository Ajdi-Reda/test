package com.codewithmosh.store.equipments;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/equipment-loans")
public class EquipmentLoanController {
    private final EquipmentLoanService equipmentLoanService;

    @GetMapping
    public Iterable<EquipmentLoanDto> getEquipmentLoans() {
        return equipmentLoanService.getEquipmentLoans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentLoanDto> getEquipmentLoan(@PathVariable Integer id) {
        var loan = equipmentLoanService.getEquipmentLoan(id);
        if (loan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loan);
    }

    @PostMapping
    public ResponseEntity<EquipmentLoanDto> createEquipmentLoan(
            @RequestBody @Valid CreateEquipmentLoanRequest request,
            UriComponentsBuilder uriBuilder) {
        var loanDto = equipmentLoanService.createEquipmentLoan(request);
        var uri = uriBuilder.path("/equipment-loans/{id}").buildAndExpand(loanDto.getId()).toUri();
        return ResponseEntity.created(uri).body(loanDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentLoanDto> updateEquipmentLoan(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateEquipmentLoanRequest request) {
        var loanDto = equipmentLoanService.updateEquipmentLoan(id, request);
        return ResponseEntity.ok(loanDto);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Void> returnEquipment(
            @PathVariable Integer id,
            @RequestParam Integer returnedToId) {
        equipmentLoanService.returnEquipment(id, returnedToId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipmentLoan(@PathVariable Integer id) {
        equipmentLoanService.deleteEquipmentLoan(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EquipmentNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleEquipmentNotAvailableException(
            EquipmentNotAvailableException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(EquipmentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEquipmentNotFoundException(
            EquipmentNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EquipmentLoanNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEquipmentLoanNotFoundException(
            EquipmentLoanNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidLoanDateException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLoanDateException(
            InvalidLoanDateException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }
}