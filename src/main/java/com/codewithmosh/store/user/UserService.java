package com.codewithmosh.store.user;

import com.codewithmosh.store.role.Role;
import com.codewithmosh.store.role.RoleNotFoundExceptionException;
import com.codewithmosh.store.role.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // Will be injected from ApplicationConfig
    private final RoleRepository roleRepository;

    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";

        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(@PathVariable Integer id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        Set<Role> roles = new HashSet<>();
        request.getRoleIds().forEach(roleId -> {
            Role role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundExceptionException::new);
            roles.add(role);
        });
        user.setRoles(roles);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto update(Integer id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        userMapper.update(request, user);

        if(request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = userMapper.mapRoleIdsToRoles(request.getRoleIds(), roleRepository);
            user.setRoles(roles);
        }
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void delete(Integer id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        userRepository.delete(user);
    }

    public void changePassword(
            @PathVariable Integer id,
            @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new AccessDeniedException();
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}