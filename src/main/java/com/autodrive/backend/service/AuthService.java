package com.autodrive.backend.service;

import com.autodrive.backend.dto.auth.*;
import com.autodrive.backend.entity.user.RefreshToken;
import com.autodrive.backend.entity.user.User;
import com.autodrive.backend.entity.user.UserRole;
import com.autodrive.backend.repo.RefreshTokenRepository;
import com.autodrive.backend.repo.UserRepository;
import com.autodrive.backend.security.JwtProperties;
import com.autodrive.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        refreshTokenRepository.deleteByUser(user); // invalidam restul de refresh

        String refreshString = jwtService.generateRefreshToken();
        RefreshToken refreshToken = RefreshToken
                .builder()
                .user(user)
                .token(refreshString)
                .expiryDate(Instant
                        .now()
                        .plusMillis(jwtProperties.refreshExpirationMs()))
                .build();
        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(jwtService.generateAccessToken(user), refreshString, "Bearer", jwtProperties.expirationMs());
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository
                .findByEmail(request.email())
                .isPresent()) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        User user = User
                .builder()
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

    public LoginResponse refresh(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.refreshToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (refreshToken.isRevoked() || refreshToken
                .getExpiryDate()
                .isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadCredentialsException("Refresh token expired or revoked");
        }

        User user = refreshToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);

        return new LoginResponse(newAccessToken, request.refreshToken(), "Bearer", jwtProperties.expirationMs());

    }

    public void logout(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.refreshToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        refreshTokenRepository.delete(refreshToken);

    }
}
