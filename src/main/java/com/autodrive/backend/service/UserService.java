package com.autodrive.backend.service;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.user.UserCreateRequest;
import com.autodrive.backend.dto.user.UserResponse;
import com.autodrive.backend.dto.user.UserUpdateRequest;
import com.autodrive.backend.entity.user.User;
import com.autodrive.backend.entity.user.UserRole;
import com.autodrive.backend.exception.ConflictException;
import com.autodrive.backend.exception.DuplicateResourceException;
import com.autodrive.backend.exception.ResourceNotFoundException;
import com.autodrive.backend.mapper.UserMapper;
import com.autodrive.backend.repo.SaleRepository;
import com.autodrive.backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private static final Set<String> SORTS = Set.of("id", "email", "firstName", "lastName", "role", "createdAt");

    private final UserRepository userRepository;
    private final SaleRepository saleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(userRepository.findAll(pageable).map(UserMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return UserMapper.toResponse(getUser(id));
    }

    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException("User already exists with email: " + request.email());
        }
        User user = UserMapper.toEntity(request);
        user.setRole(request.role() == null ? UserRole.USER : request.role());
        user.setPassword(passwordEncoder.encode(request.password()));
        return UserMapper.toResponse(userRepository.save(user));
    }

    public UserResponse update(UUID id, UserUpdateRequest request) {
        User user = getUser(id);
        if (request.email() != null && userRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) {
            throw new DuplicateResourceException("User already exists with email: " + request.email());
        }
        if (request.email() != null) user.setEmail(request.email());
        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.phone() != null) user.setPhone(request.phone());
        if (request.password() != null) user.setPassword(passwordEncoder.encode(request.password()));
        if (request.role() != null) user.setRole(request.role());
        return UserMapper.toResponse(userRepository.save(user));
    }

    public void delete(UUID id) {
        User user = userRepository.findWithEmployeeAndCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (user.getCustomer() != null || user.getEmployee() != null) {
            throw new ConflictException("Cannot delete user linked to customer or employee profile");
        }
        if (saleRepository.existsByCustomerUserId(id) || saleRepository.existsByEmployeeUserId(id)) {
            throw new ConflictException("Cannot delete user linked to sales history");
        }
        userRepository.delete(user);
    }

    private User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
