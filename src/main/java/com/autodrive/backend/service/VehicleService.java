package com.autodrive.backend.service;

import com.autodrive.backend.dto.common.PageResponse;
import com.autodrive.backend.dto.vehicle.VehicleCreateRequest;
import com.autodrive.backend.dto.vehicle.VehicleResponse;
import com.autodrive.backend.dto.vehicle.VehicleUpdateRequest;
import com.autodrive.backend.entity.vehicle.Brand;
import com.autodrive.backend.entity.vehicle.ExtraOption;
import com.autodrive.backend.entity.vehicle.Vehicle;
import com.autodrive.backend.exception.BadRequestException;
import com.autodrive.backend.exception.BrandNotFoundException;
import com.autodrive.backend.exception.ConflictException;
import com.autodrive.backend.exception.ExtraOptionNotFoundException;
import com.autodrive.backend.exception.VehicleAlreadyExistsException;
import com.autodrive.backend.exception.VehicleNotFoundException;
import com.autodrive.backend.mapper.VehicleMapper;
import com.autodrive.backend.repo.BrandRepository;
import com.autodrive.backend.repo.ExtraOptionRepository;
import com.autodrive.backend.repo.SaleRepository;
import com.autodrive.backend.repo.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final BrandRepository brandRepository;
    private final ExtraOptionRepository extraOptionRepository;
    private final SaleRepository saleRepository;
    private static final Set<String> SORTS = Set.of("vin", "model", "basePrice", "year", "status", "color", "mileage");

    @Transactional(readOnly = true)
    public PageResponse<VehicleResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(vehicleRepository.findAll(pageable).map(VehicleMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public VehicleResponse findById(String vin) {
        Vehicle vehicle = getVehicleOrThrow(vin);
        return VehicleMapper.toResponse(vehicle);

    }

    public VehicleResponse create(VehicleCreateRequest request) {

        if (vehicleRepository.existsById(request.vin())) {
            throw new VehicleAlreadyExistsException("Vehicle with VIN already exists: " + request.vin());
        }

        Vehicle vehicle = VehicleMapper.toEntity(request);

        resolveRelations(vehicle, request.brandId(), request.extraOptionIds());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return VehicleMapper.toResponse(savedVehicle);
    }

    public VehicleResponse update(String vin, VehicleUpdateRequest request) {

        Vehicle existingVehicle = getVehicleOrThrow(vin);

        VehicleMapper.applyUpdate(existingVehicle, request);

        resolveRelations(existingVehicle, request.brandId(), request.extraOptionIds());

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);

        return VehicleMapper.toResponse(updatedVehicle);

    }

    public void delete(String vin) {
        if (!vehicleRepository.existsById(vin)) {
            throw new VehicleNotFoundException("Vehicle not found with VIN: " + vin);
        }
        if (saleRepository.existsByVehicleVin(vin)) {
            throw new ConflictException("Cannot delete vehicle referenced by sale contract");
        }
        vehicleRepository.deleteById(vin);
    }


    private Vehicle getVehicleOrThrow(String vin) {
        return vehicleRepository
                .findById(vin)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with VIN: " + vin));
    }

    private void resolveRelations(Vehicle vehicle, UUID brandId, List<UUID> optionIds) {
        if (brandId != null) {
            Brand brand = brandRepository
                    .findById(brandId)
                    .orElseThrow(() -> new BrandNotFoundException("Brand not found with id: " + brandId));
            vehicle.setBrand(brand);
        }

        if (optionIds != null) {
            if (optionIds.isEmpty()) {
                // if they passed an empty list, they want to clear the options
                vehicle.setExtraOptions(new HashSet<>());
            } else {
                Set<UUID> uniqueOptionIds = new HashSet<>(optionIds);
                if (uniqueOptionIds.size() != optionIds.size()) {
                    throw new BadRequestException("Duplicate extra option IDs are not allowed");
                }
                Set<ExtraOption> foundOptions = new HashSet<>(extraOptionRepository.findAllById(uniqueOptionIds));
                if (foundOptions.size() != uniqueOptionIds.size()) {
                    throw new ExtraOptionNotFoundException("One or more extra options were not found");
                }
                vehicle.setExtraOptions(foundOptions);
            }
        }
    }
}
