package com.autodrive.backend.security;

import com.autodrive.backend.entity.user.Employee;
import com.autodrive.backend.entity.user.EmployeePosition;
import com.autodrive.backend.entity.user.User;
import com.autodrive.backend.entity.user.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserAuthoritiesServiceTest {

    private final UserAuthoritiesService userAuthoritiesService = new UserAuthoritiesService();

    @Test
    void shouldAddRoleAndManagerPositionAuthorities() {
        User user = User.builder()
                .role(UserRole.USER)
                .employee(Employee.builder().position(EmployeePosition.MANAGER).build())
                .build();

        Collection<String> authorities = userAuthoritiesService.getAuthorities(user)
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertTrue(authorities.contains("ROLE_USER"));
        assertTrue(authorities.contains("POSITION_MANAGER"));
    }

    @Test
    void shouldOnlyAddRoleAuthorityForNonEmployee() {
        User user = User.builder()
                .role(UserRole.USER)
                .build();

        Collection<String> authorities = userAuthoritiesService.getAuthorities(user)
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertTrue(authorities.contains("ROLE_USER"));
        assertTrue(authorities.stream().noneMatch(authority -> authority.startsWith("POSITION_")));
    }
}
