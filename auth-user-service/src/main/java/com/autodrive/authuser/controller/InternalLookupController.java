package com.autodrive.authuser.controller;

import com.autodrive.authuser.repo.CustomerRepository;
import com.autodrive.authuser.repo.EmployeeRepository;
import com.autodrive.authuser.service.InternalTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal")
public class InternalLookupController {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final InternalTokenService internalTokenService;

    @GetMapping("/customers/{id}/exists")
    public boolean customerExists(@PathVariable UUID id, @RequestHeader(name = "X-Internal-Token", required = false) String token) {
        internalTokenService.validate(token);
        return customerRepository.existsById(id);
    }

    @GetMapping("/employees/{id}/exists")
    public boolean employeeExists(@PathVariable UUID id, @RequestHeader(name = "X-Internal-Token", required = false) String token) {
        internalTokenService.validate(token);
        return employeeRepository.existsById(id);
    }
}
