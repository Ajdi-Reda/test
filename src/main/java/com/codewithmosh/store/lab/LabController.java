package com.codewithmosh.store.lab;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labs")
public class LabController {

    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping
    public List<LabDto> getAllLabs() {
        return labService.getAllLabs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabDto> getLabById(@PathVariable Integer id) {
        return labService.getLabById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/cnt")
    public ResponseEntity<Long> getLabCount(){
        return ResponseEntity.ok(labService.countNumberLabs());
    }
}
