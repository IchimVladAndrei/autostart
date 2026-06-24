package com.autodrive.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customers")
public class Customer {

    @Id
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "CNP is required")
    @Pattern(regexp = "^\\d{13}$", message = "CNP must contain exactly 13 digits")
    @Column(unique = true, nullable = false, length = 13)
    private String cnp;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    @Pattern(regexp = "^[0-9+()\\s-]+$", message = "Phone number format invalid")
    private String phone;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private CustomerStatus status;
}
