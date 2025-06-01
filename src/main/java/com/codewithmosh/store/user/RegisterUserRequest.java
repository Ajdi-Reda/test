package com.codewithmosh.store.user;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty(message = "At least one role must be selected")
    private Set<@NotNull(message = "Role ID cannot be null") Integer> roleIds;
}
