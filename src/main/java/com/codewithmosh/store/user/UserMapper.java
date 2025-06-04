package com.codewithmosh.store.user;

import com.codewithmosh.store.role.Role;
import com.codewithmosh.store.role.RoleRepository;
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
    User toEntity(CheckUserTokenRequest request);
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

