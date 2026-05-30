package com.autodrive.backend.entity.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    private String country;

    @NotNull(message = "Founded year is required")
    @Min(value = 1800, message = "Founded year must be greater than or equal to 1800")
    private Integer foundedYear;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();
}
