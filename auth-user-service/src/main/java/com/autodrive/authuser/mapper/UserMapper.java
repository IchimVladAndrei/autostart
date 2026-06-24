package com.autodrive.authuser.mapper;

import com.autodrive.authuser.dto.user.UserCreateRequest;
import com.autodrive.authuser.dto.user.UserResponse;
import com.autodrive.authuser.dto.user.UserUpdateRequest;
import com.autodrive.authuser.entity.user.User;
import com.autodrive.authuser.entity.user.UserRole;

public final class UserMapper {
    private UserMapper() {
    }

    public static User toEntity(UserCreateRequest req) {
        return User.builder()
                .email(req.email())
                .firstName(req.firstName())
                .lastName(req.lastName())
                .phone(req.phone())
                .password(req.password())
                .role(req.role() == null ? UserRole.USER : req.role())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public static void applyUpdates(User user, UserUpdateRequest req) {
        if (req.email() != null) user.setEmail(req.email());
        if (req.firstName() != null) user.setFirstName(req.firstName());
        if (req.lastName() != null) user.setLastName(req.lastName());
        if (req.phone() != null) user.setPhone(req.phone());
        if (req.password() != null) user.setPassword(req.password());
        if (req.role() != null) user.setRole(req.role());
    }
}
