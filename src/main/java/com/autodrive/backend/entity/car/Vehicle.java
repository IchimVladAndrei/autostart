package com.autodrive.backend.entity.car;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @NotBlank(message = "VIN is required")
    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    @Column(unique = true, nullable = false, length = 17)
    private String vin;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be strictly positive")
    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be valid")
    private Integer year;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Mileage is required")
    @PositiveOrZero(message = "Mileage cannot be negative")
    private Integer mileage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @JsonIgnoreProperties({"vehicles"})
    private Brand brand;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "vehicle_extra_option",
            joinColumns = @JoinColumn(name = "vehicle_vin"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    @JsonIgnoreProperties({"vehicles"})
    private List<ExtraOption> extraOptions = new ArrayList<>();
}
