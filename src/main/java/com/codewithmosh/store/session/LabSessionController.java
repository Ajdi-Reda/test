package com.codewithmosh.store.session;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@AllArgsConstructor
public class LabSessionController {

    private final LabSessionService labSessionService;

    @GetMapping
    public ResponseEntity<List<LabSessionDto>> getAllSessions() {
        List<LabSessionDto> sessions = labSessionService.findAll();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabSessionDto> getSessionById(@PathVariable Integer id) {
        LabSessionDto session = labSessionService.findById(id);
        return ResponseEntity.ok(session);
    }

    @PostMapping
    public ResponseEntity<LabSessionDto> createSession(@Valid @RequestBody LabSessionCreateRequest request) {
        LabSessionDto created = labSessionService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabSessionDto> updateSession(@PathVariable Integer id, @Valid @RequestBody LabSessionUpdateRequest request) {
        LabSessionDto updated = labSessionService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Integer id) {
        labSessionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
