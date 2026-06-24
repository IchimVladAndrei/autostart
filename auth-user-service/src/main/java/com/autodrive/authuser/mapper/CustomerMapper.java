package com.autodrive.authuser.mapper;

import com.autodrive.authuser.dto.user.CustomerCreateRequest;
import com.autodrive.authuser.dto.user.CustomerResponse;
import com.autodrive.authuser.dto.user.CustomerUpdateRequest;
import com.autodrive.authuser.entity.user.Customer;

public final class CustomerMapper {

    private CustomerMapper() {

    }

    public static Customer toEntity(CustomerCreateRequest req) {
        return Customer
                .builder()
                .cnp(req.cnp())
                .name(req.name())
                .phone(req.phone())
                .status(req.status())
                .build();
    }

    public static CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(c.getName(), c.getCnp(), c.getPhone(), c.getStatus(), c.getUserId());
    }


    public static void applyUpdates(Customer c, CustomerUpdateRequest req) {
        if (req.name() != null) c.setName(req.name());

        if (req.cnp() != null) c.setCnp(req.cnp());

        if (req.phone() != null) c.setPhone(req.phone());

        if (req.status() != null) c.setStatus(req.status());

    }

}
