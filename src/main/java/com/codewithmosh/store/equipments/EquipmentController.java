package com.codewithmosh.store.equipments;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
@RequestMapping("/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentLoanRepository equipmentloanRepository;

    @GetMapping
    public Iterable<EquipmentDto> getEquipments() {
        return equipmentService.getEquipments();
    }

    @GetMapping("/available")
    public ResponseEntity<List<EquipmentDto>> getAvailableEquipmentsAt(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime datetime) {

        Instant pointInTime = datetime.atZone(ZoneId.systemDefault()).toInstant();

        List<Equipmentloan> loansAtTime = equipmentloanRepository.findLoansAtTime(pointInTime);

        Set<Integer> loanedEquipmentIds = loansAtTime.stream()
                .map(loan -> loan.getEquipment().getId())
                .collect(Collectors.toSet());

        List<EquipmentDto> allEquipments = StreamSupport.stream(equipmentService.getEquipments().spliterator(), false)
                .collect(Collectors.toList());

        List<EquipmentDto> availableEquipments = allEquipments.stream()
                .filter(e -> !loanedEquipmentIds.contains(e.getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(availableEquipments);
    }



    @GetMapping("/cnt")
    public ResponseEntity<Long> getGroupCount() {
        long count = equipmentService.countNumberEquipments();
        return ResponseEntity.ok(count);
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
