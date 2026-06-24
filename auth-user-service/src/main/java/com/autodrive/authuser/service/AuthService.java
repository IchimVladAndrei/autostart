package com.autodrive.authuser.service;

import com.autodrive.authuser.dto.auth.*;
import com.autodrive.authuser.entity.user.RefreshToken;
import com.autodrive.authuser.entity.user.User;
import com.autodrive.authuser.entity.user.UserRole;
import com.autodrive.authuser.repo.RefreshTokenRepository;
import com.autodrive.authuser.repo.UserRepository;
import com.autodrive.authuser.security.UserAuthoritiesService;
import com.autodrive.common.security.JwtProperties;
import com.autodrive.common.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
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
    private final UserAuthoritiesService userAuthoritiesService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        long refreshExpirationMs = request.rememberMe()
                ? jwtProperties.rememberMeRefreshExpirationMs()
                : jwtProperties.refreshExpirationMs();
        String refreshString = jwtService.generateRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository
                .findByUser(user)
                .orElseGet(() -> RefreshToken
                        .builder()
                        .user(user)
                        .build());
        refreshToken.setToken(refreshString);
        refreshToken.setExpiryDate(Instant
                .now()
                .plusMillis(refreshExpirationMs));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(jwtService.generateAccessToken(
                user.getEmail(),
                user.getRole().name(),
                userAuthoritiesService.getAuthorities(user)
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        ), refreshString, "Bearer", jwtProperties.expirationMs());
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
                .password(passwordEncoder.encode(request.password()))
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
        String newAccessToken = jwtService.generateAccessToken(
                user.getEmail(),
                user.getRole().name(),
                userAuthoritiesService.getAuthorities(user)
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return new LoginResponse(newAccessToken, request.refreshToken(), "Bearer", jwtProperties.expirationMs());

    }

    public void logout(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.refreshToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        refreshTokenRepository.delete(refreshToken);

    }
}
