package com.autodrive.backend.mapper;

import com.autodrive.backend.dto.sale.SaleContractCreateRequest;
import com.autodrive.backend.dto.sale.SaleContractResponse;
import com.autodrive.backend.dto.sale.SaleContractUpdateRequest;
import com.autodrive.backend.entity.sale.SaleContract;

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
                .build();
    }

    public static SaleContractResponse toResponse(SaleContract sc) {
        return new SaleContractResponse(
                sc.getContractDate(),
                sc.getSalePrice(),
                sc.getNotes(),
                sc.getStatus(),
                sc.getId(),
                sc.getVehicle() != null ? sc
                                          .getVehicle()
                                          .getVin() : null,
                sc.getCustomer() != null ? sc
                                           .getCustomer()
                                           .getUserId() : null,
                sc.getEmployee() != null ? sc
                                           .getEmployee()
                                           .getUserId() : null

        );
    }

    public static void applyUpdates(SaleContract sc, SaleContractUpdateRequest req) {
        if (req.contractDate() != null) sc.setContractDate(req.contractDate());
        if (req.salePrice() != null) sc.setSalePrice(req.salePrice());
        if (req.notes() != null) sc.setNotes(req.notes());
        if (req.status() != null) sc.setStatus(req.status());
    }
}
