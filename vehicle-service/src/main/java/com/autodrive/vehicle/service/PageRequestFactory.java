package com.autodrive.vehicle.service;

import com.autodrive.common.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

final class PageRequestFactory {
    private PageRequestFactory() {
    }

    static Pageable create(int page, int size, String sortBy, String direction, Set<String> allowedSorts) {
        if (page < 0) {
            throw new BadRequestException("Page index must not be negative");
        }
        if (size < 1 || size > 100) {
            throw new BadRequestException("Page size must be between 1 and 100");
        }
        if (!allowedSorts.contains(sortBy)) {
            throw new BadRequestException("Invalid sort property: " + sortBy);
        }
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid sort direction: " + direction);
        }
        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }
}
