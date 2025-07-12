package com.codewithmosh.store.user;

import com.codewithmosh.store.common.EntityNotFoundException;
import com.codewithmosh.store.common.PasswordGenerator;
import com.codewithmosh.store.email.EmailSenderService;
import com.codewithmosh.store.invitation.InvalidTokenException;
import com.codewithmosh.store.invitation.InvitationTokenRepository;
import com.codewithmosh.store.invitation.InvitationTokenService;
import com.codewithmosh.store.role.Role;
import com.codewithmosh.store.role.RoleNotFoundExceptionException;
import com.codewithmosh.store.role.RoleRepository;
import jakarta.transaction.Transactional;
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
    private InvitationTokenService invitationTokenService;
    private EmailSenderService emailSenderService;
    private InvitationTokenRepository invitationTokenRepository;

    public long countNumberUsers() {
        return userRepository.count();
    }

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
            throw new EntityNotFoundException("User not found");
        }

        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(PasswordGenerator.generatePassword(10)));


        Set<Role> roles = new HashSet<>();
        request.getRoleIds().forEach(roleId -> {
            Role role = roleRepository.findById(roleId).orElseThrow(RoleNotFoundExceptionException::new);
            roles.add(role);
        });
        user.setRoles(roles);
        userRepository.save(user);

        String token = invitationTokenService.createInvitationToken(user);
        String body = String.format("""
Hello %s,

You have been invited to join our Laboratory Management System.

To complete your registration:

1. Open the Laboratory Management desktop application.
2. Go to the registration page.
3. Enter your email address: %s
4. Enter the following invitation token: %s

This platform will allow you to manage experiments, schedule lab usage, track inventory, and collaborate with your colleagues efficiently.

If you did not expect this invitation or have any questions, please contact your lab administrator or our support team.

Thank you,
Lab Admin Team
""", user.getName(), user.getEmail(), token);


        emailSenderService.sendEmail(user.getEmail(), "Register you account", body);
        return userMapper.toDto(user);
    }

    public UserDto update(Integer id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        userMapper.update(request, user);

        if(request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = userMapper.mapRoleIdsToRoles(request.getRoleIds(), roleRepository);
            user.setRoles(roles);
        }
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public Boolean checkToken(String email, String token) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        var userWithToken = invitationTokenRepository.findByToken(token).orElseThrow(InvalidTokenException::new);

        if(!userWithToken.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Invalid token or user");
        }

        return true;
    }

    @Transactional
    public void registerEmployeeByToken(RegisterUserByTokenRequest request) {
        boolean isValid = checkToken(request.getEmail(), request.getToken());
        if(isValid) {
        if(!request.getPassword().equals(request.getConfirmPassword()))
            throw new PasswordsNotMatchException();

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        invitationTokenRepository.deleteByToken(request.getToken());
        }
    }

    public void delete(Integer id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        userRepository.delete(user);
    }


    public void changePassword(
            @PathVariable Integer id,
            @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
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