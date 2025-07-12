package com.codewithmosh.store.auth;

import com.codewithmosh.store.user.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Integer) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }

    public JwtResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        System.out.println("🔐 Attempting login for: " + request.getEmail());

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("✅ User found: " + user.getEmail());
        System.out.println("✅ Encoded password in DB: " + user.getPassword());
        System.out.println("✅ Raw password in request: " + request.getPassword());
        // Only for debugging — remove this in production
        System.out.println("✅ Matches? " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        // Add this temporary debug code in your login method
        String testPassword = "123456";
        String storedHash = "$2a$10$B5Lb6m98Qh0saQPV8rQejO/yUlia4V1KN05hjqsW5z8YKgNs2l0Aa";
        System.out.println("🧪 Testing stored hash with '123456': " + passwordEncoder.matches(testPassword, storedHash));

// Also test encoding a new password
        String newlyEncoded = passwordEncoder.encode("123456");
        System.out.println("🧪 Newly encoded '123456': " + newlyEncoded);
        System.out.println("🧪 New hash matches '123456': " + passwordEncoder.matches("123456", newlyEncoded));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword() // plain password here
                )
        );

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(accessToken.toString());
    }


    public UserDto me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Integer) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        return userMapper.toDto(user);
    }

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
}
