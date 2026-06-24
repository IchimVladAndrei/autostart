package com.autodrive.sales.mapper;

import com.autodrive.sales.dto.sale.SaleContractCreateRequest;
import com.autodrive.sales.dto.sale.SaleContractResponse;
import com.autodrive.sales.dto.sale.SaleContractUpdateRequest;
import com.autodrive.sales.entity.sale.SaleContract;

public final class SaleContractMapper {
    private SaleContractMapper() {
    }

    public static SaleContract toEntity(SaleContractCreateRequest req) {
        return SaleContract
                .builder()
                .contractDate(req.contractDate())
                .salePrice(req.salePrice())
                .notes(req.notes())
                .status(req.status())
                .vehicleVin(req.vehicleVin())
                .customerId(req.customerId())
                .employeeId(req.employeeId())
                .build();
    }

    public static SaleContractResponse toResponse(SaleContract sc) {
        return new SaleContractResponse(
                sc.getContractDate(),
                sc.getSalePrice(),
                sc.getNotes(),
                sc.getStatus(),
                sc.getId(),
                sc.getVehicleVin(),
                sc.getCustomerId(),
                sc.getEmployeeId()

        );
    }

    public static void applyUpdates(SaleContract sc, SaleContractUpdateRequest req) {
        if (req.contractDate() != null) sc.setContractDate(req.contractDate());
        if (req.salePrice() != null) sc.setSalePrice(req.salePrice());
        if (req.notes() != null) sc.setNotes(req.notes());
        if (req.status() != null) sc.setStatus(req.status());
    }
}
