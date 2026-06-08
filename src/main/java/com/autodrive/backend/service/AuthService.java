package com.autodrive.backend.service;

import com.autodrive.backend.dto.auth.LoginRequest;
import com.autodrive.backend.dto.auth.LoginResponse;
import com.autodrive.backend.dto.auth.RegisterRequest;
import com.autodrive.backend.dto.auth.RegisterResponse;
import com.autodrive.backend.entity.user.User;
import com.autodrive.backend.entity.user.UserRole;
import com.autodrive.backend.repo.UserRepository;
import com.autodrive.backend.security.JwtProperties;
import com.autodrive.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new LoginResponse(jwtService.generateAccessToken(user), "Bearer", jwtProperties.expirationMs());
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        User user = User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .password(request.password())
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }
}
