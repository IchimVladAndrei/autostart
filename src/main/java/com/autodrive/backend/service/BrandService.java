package com.autodrive.backend.service;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.vehicle.BrandCreateRequest;
import com.autodrive.backend.dto.vehicle.BrandResponse;
import com.autodrive.backend.dto.vehicle.BrandUpdateRequest;
import com.autodrive.backend.entity.vehicle.Brand;
import com.autodrive.backend.exception.ConflictException;
import com.autodrive.backend.exception.DuplicateResourceException;
import com.autodrive.backend.exception.ResourceNotFoundException;
import com.autodrive.backend.mapper.BrandMapper;
import com.autodrive.backend.repo.BrandRepository;
import com.autodrive.backend.repo.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandService {
    private static final Set<String> SORTS = Set.of("id", "name", "country", "foundedYear");

    private final BrandRepository brandRepository;
    private final VehicleRepository vehicleRepository;

    @Transactional(readOnly = true)
    public PageResponse<BrandResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(brandRepository.findAll(pageable).map(BrandMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public BrandResponse findById(UUID id) {
        return BrandMapper.toResponse(getBrand(id));
    }

    public BrandResponse create(BrandCreateRequest request) {
        if (brandRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Brand already exists with name: " + request.name());
        }
        return BrandMapper.toResponse(brandRepository.save(BrandMapper.toEntity(request)));
    }

    public BrandResponse update(UUID id, BrandUpdateRequest request) {
        Brand brand = getBrand(id);
        if (request.name() != null && brandRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new DuplicateResourceException("Brand already exists with name: " + request.name());
        }
        BrandMapper.applyUpdate(brand, request);
        return BrandMapper.toResponse(brandRepository.save(brand));
    }

    public void delete(UUID id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found with id: " + id);
        }
        if (vehicleRepository.existsByBrandId(id)) {
            throw new ConflictException("Cannot delete brand referenced by vehicles");
        }
        brandRepository.deleteById(id);
    }

    private Brand getBrand(UUID id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
    }
}
