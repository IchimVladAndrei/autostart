package com.autodrive.backend.security;

import com.autodrive.backend.entity.user.User;
import com.autodrive.backend.exception.ErrorDetails;
import com.autodrive.backend.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserAuthoritiesService userAuthoritiesService;

    // helper scriere errdetails JSON in HTTP respnse
    public static void writeErrorResponse(HttpServletResponse response, int status, String message, String path) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setCharacterEncoding("UTF-8");

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                message,
                "uri=" + path,
                null
        );


        //scriere safe pt timestamp
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonResponse = objectMapper.writeValueAsString(errorDetails);
        response
                .getWriter()
                .write(jsonResponse);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        try {
            String email = jwtService.extractUsername(token);
            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {
                userRepository
                        .findByEmail(email)
                        .filter(user -> jwtService.isTokenValid(token, user))
                        .ifPresent(this::authenticate);
            }
        } catch (ExpiredJwtException ex) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired", request.getRequestURI());
            return;
        } catch (MalformedJwtException | IllegalArgumentException ex) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or malformed JWT format", request.getRequestURI());
            return;
        }
    }

    private void authenticate(User user) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                userAuthoritiesService.getAuthorities(user)
        );
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }
}
