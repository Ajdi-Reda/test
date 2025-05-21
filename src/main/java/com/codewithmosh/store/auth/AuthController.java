package com.codewithmosh.store.auth;

import com.codewithmosh.store.user.LoginRequest;
import com.codewithmosh.store.user.UserDto;
import com.codewithmosh.store.user.UserMapper;
import com.codewithmosh.store.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse response) {
        JwtResponse jwtResponse = authService.login(request, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
        @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var userDto = authService.me();
        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}
