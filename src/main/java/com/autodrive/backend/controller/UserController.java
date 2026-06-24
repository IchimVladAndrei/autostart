package com.autodrive.backend.controller;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.user.UserCreateRequest;
import com.autodrive.backend.dto.user.UserResponse;
import com.autodrive.backend.dto.user.UserUpdateRequest;
import com.autodrive.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
public class UserController {
    private final UserService userService;

    @GetMapping
    public PageResponse<UserResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "email") String sortBy,
                                             @RequestParam(defaultValue = "asc") String direction) {
        return userService.findAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
