package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.requests.ChangePasswordRequest;
import com.codewithmosh.store.dtos.requests.RegisterUserRequest;
import com.codewithmosh.store.dtos.requests.UpdateUserRequest;
import com.codewithmosh.store.dtos.response.UserDto;
import com.codewithmosh.store.exceptions.AccessDeniedException;
import com.codewithmosh.store.exceptions.RoleNotFoundExceptionException;
import com.codewithmosh.store.exceptions.UserAlreadyExistsException;
import com.codewithmosh.store.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Iterable<UserDto> getAllUsers(
        @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        return userService.getAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        var userDto = userService.getUser(id);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) {

        var userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
        @PathVariable(name = "id") Integer id,
        @RequestBody UpdateUserRequest request) {
        var userDto = userService.update(id, request);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Integer id,
            @RequestBody ChangePasswordRequest request) {

        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(RoleNotFoundExceptionException.class)
    public ResponseEntity<Map<String, String>> handleRoleNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Role not found"));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException() {
        return ResponseEntity.badRequest().body(Map.of("error", "User already exists"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
