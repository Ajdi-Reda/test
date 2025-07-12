package com.codewithmosh.store.student_group;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students-group")
@AllArgsConstructor
public class StudentsGroupController {

    private final StudentsGroupService studentsGroupService;

    @GetMapping("/cnt")
    public ResponseEntity<Long> getLabCount(){
        return ResponseEntity.ok(studentsGroupService.countNumberStudentsGroups());
    }

    @GetMapping
    public List<StudentsGroupDto> findAll() {
        return studentsGroupService.findAll();
    }

    @GetMapping("/{id}")
    public StudentsGroupDto findById(@PathVariable Integer id) {
        return studentsGroupService.findById(id);
    }

    @PostMapping
    public ResponseEntity<StudentsGroupDto> create(@RequestBody @Valid CreateStudentsGroupRequest request) {
        StudentsGroupDto dto = studentsGroupService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public StudentsGroupDto update(@PathVariable Integer id, @RequestBody @Valid UpdateStudentsGroupRequest request) {
        return studentsGroupService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        studentsGroupService.delete(id);
    }

}
