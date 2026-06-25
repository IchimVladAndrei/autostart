package com.autodrive.vehicle.service;

import com.autodrive.common.dto.PageResponse;
import com.autodrive.vehicle.dto.vehicle.BrandCreateRequest;
import com.autodrive.vehicle.dto.vehicle.BrandResponse;
import com.autodrive.vehicle.dto.vehicle.BrandUpdateRequest;
import com.autodrive.vehicle.entity.vehicle.Brand;
import com.autodrive.common.exception.ConflictException;
import com.autodrive.common.exception.DuplicateResourceException;
import com.autodrive.common.exception.ResourceNotFoundException;
import com.autodrive.vehicle.mapper.BrandMapper;
import com.autodrive.vehicle.repo.BrandRepository;
import com.autodrive.vehicle.repo.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(cacheNames = "brands", key = "'list:' + #page + ':' + #size + ':' + #sortBy + ':' + #direction")
    public PageResponse<BrandResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(brandRepository.findAll(pageable).map(BrandMapper::toResponse));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "brands", key = "'id:' + #id")
    public BrandResponse findById(UUID id) {
        return BrandMapper.toResponse(getBrand(id));
    }

    @CacheEvict(cacheNames = {"brands", "vehicle"}, allEntries = true)
    public BrandResponse create(BrandCreateRequest request) {
        if (brandRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Brand already exists with name: " + request.name());
        }
        return BrandMapper.toResponse(brandRepository.save(BrandMapper.toEntity(request)));
    }

    @CacheEvict(cacheNames = {"brands", "vehicle"}, allEntries = true)
    public BrandResponse update(UUID id, BrandUpdateRequest request) {
        Brand brand = getBrand(id);
        if (request.name() != null && brandRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new DuplicateResourceException("Brand already exists with name: " + request.name());
        }
        BrandMapper.applyUpdate(brand, request);
        return BrandMapper.toResponse(brandRepository.save(brand));
    }

    @CacheEvict(cacheNames = {"brands", "vehicle"}, allEntries = true)
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
