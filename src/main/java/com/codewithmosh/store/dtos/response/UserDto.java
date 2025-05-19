package com.codewithmosh.store.dtos.response;

import com.codewithmosh.store.entities.ChemicalUsage;
import com.codewithmosh.store.entities.Role;
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
    private Set<ChemicalUsage> usages;
}
