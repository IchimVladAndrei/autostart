package com.autodrive.vehicle.service;

import com.autodrive.common.dto.PageResponse;
import com.autodrive.vehicle.dto.vehicle.ExtraOptionCreateRequest;
import com.autodrive.vehicle.dto.vehicle.ExtraOptionResponse;
import com.autodrive.vehicle.dto.vehicle.ExtraOptionUpdateRequest;
import com.autodrive.vehicle.entity.vehicle.ExtraOption;
import com.autodrive.common.exception.ConflictException;
import com.autodrive.common.exception.DuplicateResourceException;
import com.autodrive.common.exception.ResourceNotFoundException;
import com.autodrive.vehicle.mapper.ExtraOptionMapper;
import com.autodrive.vehicle.repo.ExtraOptionRepository;
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
public class ExtraOptionService {
    private static final Set<String> SORTS = Set.of("id", "name", "price");

    private final ExtraOptionRepository extraOptionRepository;
    private final VehicleRepository vehicleRepository;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "options", key = "'list:' + #page + ':' + #size + ':' + #sortBy + ':' + #direction")
    public PageResponse<ExtraOptionResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(extraOptionRepository.findAll(pageable).map(ExtraOptionMapper::toResponse));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "options", key = "'id:' + #id")
    public ExtraOptionResponse findById(UUID id) {
        return ExtraOptionMapper.toResponse(getOption(id));
    }

    @CacheEvict(cacheNames = {"options", "vehicle"}, allEntries = true)
    public ExtraOptionResponse create(ExtraOptionCreateRequest request) {
        if (extraOptionRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Extra option already exists with name: " + request.name());
        }
        return ExtraOptionMapper.toResponse(extraOptionRepository.save(ExtraOptionMapper.toEntity(request)));
    }

    @CacheEvict(cacheNames = {"options", "vehicle"}, allEntries = true)
    public ExtraOptionResponse update(UUID id, ExtraOptionUpdateRequest request) {
        ExtraOption option = getOption(id);
        if (request.name() != null && extraOptionRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new DuplicateResourceException("Extra option already exists with name: " + request.name());
        }
        ExtraOptionMapper.applyUpdate(option, request);
        return ExtraOptionMapper.toResponse(extraOptionRepository.save(option));
    }

    @CacheEvict(cacheNames = {"options", "vehicle"}, allEntries = true)
    public void delete(UUID id) {
        if (!extraOptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Extra option not found with id: " + id);
        }
        if (vehicleRepository.existsByExtraOptionsId(id)) {
            throw new ConflictException("Cannot delete extra option assigned to vehicles");
        }
        extraOptionRepository.deleteById(id);
    }

    private ExtraOption getOption(UUID id) {
        return extraOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Extra option not found with id: " + id));
    }
}
