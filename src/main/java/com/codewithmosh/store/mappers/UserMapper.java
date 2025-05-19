package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.requests.RegisterUserRequest;
import com.codewithmosh.store.dtos.requests.UpdateUserRequest;
import com.codewithmosh.store.dtos.response.UserDto;
import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);

    default Set<Role> mapRoleIdsToRoles(Set<Integer> roleIds, RoleRepository roleRepository) {
        if (roleIds == null) {
            return new HashSet<>();
        }
        return roleIds.stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
}
}

