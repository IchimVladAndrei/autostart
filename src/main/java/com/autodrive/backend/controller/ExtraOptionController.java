package com.autodrive.backend.controller;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.vehicle.ExtraOptionCreateRequest;
import com.autodrive.backend.dto.vehicle.ExtraOptionResponse;
import com.autodrive.backend.dto.vehicle.ExtraOptionUpdateRequest;
import com.autodrive.backend.service.ExtraOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/extra-options")
public class ExtraOptionController {
    private final ExtraOptionService extraOptionService;

    @GetMapping
    public PageResponse<ExtraOptionResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String direction) {
        return extraOptionService.findAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public ExtraOptionResponse getById(@PathVariable UUID id) {
        return extraOptionService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ResponseEntity<ExtraOptionResponse> create(@Valid @RequestBody ExtraOptionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extraOptionService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ExtraOptionResponse update(@PathVariable UUID id, @Valid @RequestBody ExtraOptionUpdateRequest request) {
        return extraOptionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('POSITION_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        extraOptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
