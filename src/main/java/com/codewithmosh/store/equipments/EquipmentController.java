package com.codewithmosh.store.equipments;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public Iterable<EquipmentDto> getEquipments() {
        return equipmentService.getEquipments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDto> getEquipment(@PathVariable Integer id) {
        var equipment = equipmentService.getEquipment(id);
        if (equipment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    public ResponseEntity<EquipmentDto> addEquipment(@RequestBody @Valid CreateEquipmentRequest request, UriComponentsBuilder uriBuilder) {
        var equipmentDto = equipmentService.createEquipment(request);
        var uri = uriBuilder.path("/equipments/{id}").buildAndExpand(equipmentDto.getId()).toUri();
        return ResponseEntity.created(uri).body(equipmentDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDto> updateEquipment(@PathVariable Integer id, @RequestBody @Valid UpdateEquipmentRequest request) {
        var equipmentDto = equipmentService.updateEquipment(id, request);
        if (equipmentDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(equipmentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Integer id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EquipmentAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEquipmentAlreadyExistsException(EquipmentAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", "equipment already exists"));
    }
}
