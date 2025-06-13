package com.codewithmosh.store.group;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public List<GroupDto> getAll() {
        return groupService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getById(@PathVariable Integer id) {
        GroupDto groupDto = groupService.findById(id);
        if (groupDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groupDto);
    }

    @PostMapping
    public ResponseEntity<GroupDto> create(@Validated @RequestBody @Valid GroupCreateRequest request, UriComponentsBuilder uriBuilder) {
        GroupDto createdGroup = groupService.create(request);
        URI location = uriBuilder.path("/groups/{id}").buildAndExpand(createdGroup.getId()).toUri();
        return ResponseEntity.created(location).body(createdGroup);
    }
}
