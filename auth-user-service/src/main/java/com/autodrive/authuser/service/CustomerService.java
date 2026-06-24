package com.autodrive.authuser.service;

import com.autodrive.common.dto.PageResponse;
import com.autodrive.authuser.dto.user.CustomerCreateRequest;
import com.autodrive.authuser.dto.user.CustomerResponse;
import com.autodrive.authuser.dto.user.CustomerUpdateRequest;
import com.autodrive.authuser.entity.user.Customer;
import com.autodrive.authuser.entity.user.User;
import com.autodrive.common.exception.ConflictException;
import com.autodrive.common.exception.DuplicateResourceException;
import com.autodrive.common.exception.ResourceNotFoundException;
import com.autodrive.authuser.mapper.CustomerMapper;
import com.autodrive.authuser.repo.CustomerRepository;
import com.autodrive.authuser.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private static final Set<String> SORTS = Set.of("userId", "name", "cnp", "phone", "status");

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> findAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequestFactory.create(page, size, sortBy, direction, SORTS);
        return PageResponse.from(customerRepository.findAll(pageable).map(CustomerMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        return CustomerMapper.toResponse(getCustomer(id));
    }

    public CustomerResponse create(CustomerCreateRequest request) {
        if (customerRepository.existsById(request.userId())) {
            throw new ConflictException("User already has a customer profile");
        }
        if (customerRepository.existsByCnp(request.cnp())) {
            throw new DuplicateResourceException("Customer already exists with CNP: " + request.cnp());
        }
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));
        Customer customer = CustomerMapper.toEntity(request);
        customer.setUser(user);
        user.setCustomer(customer);
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    public CustomerResponse update(UUID id, CustomerUpdateRequest request) {
        Customer customer = getCustomer(id);
        if (request.cnp() != null && customerRepository.existsByCnpAndUserIdNot(request.cnp(), id)) {
            throw new DuplicateResourceException("Customer already exists with CNP: " + request.cnp());
        }
        CustomerMapper.applyUpdates(customer, request);
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    public void delete(UUID id) {
        Customer customer = getCustomer(id);
        if (customer.getUser() != null) {
            customer.getUser().setCustomer(null);
        }
        customerRepository.delete(customer);
    }

    private Customer getCustomer(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }
}
