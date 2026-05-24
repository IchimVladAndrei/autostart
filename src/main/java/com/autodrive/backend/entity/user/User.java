package com.autodrive.backend.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message="temp")
    @Email(message="temp")
    @Column(unique = true,nullable = false)
    private String email;

    @NotBlank(message = "temp")
    @Size(min = 6, message = "temp")
    @Column(nullable = false)
    private String password;
    @NotBlank(message = "temp")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @NotBlank(message = "temp")
    @Column(name="last_name",nullable=false)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private EmployeeProfile employeeProfile;

}
