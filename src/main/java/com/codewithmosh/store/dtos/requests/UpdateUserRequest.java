package com.codewithmosh.store.dtos.requests;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    public String name;
    public String email;
    public Set<Integer> roleIds;
}
