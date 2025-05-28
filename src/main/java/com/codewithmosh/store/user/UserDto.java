package com.codewithmosh.store.user;

import com.codewithmosh.store.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private Set<Role> roles;
}
