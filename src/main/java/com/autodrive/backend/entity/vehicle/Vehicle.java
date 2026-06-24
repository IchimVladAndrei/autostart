package com.autodrive.backend.entity.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @Setter(AccessLevel.NONE)
    @NotBlank(message = "VIN is required")
    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    @Column(unique = true, nullable = false, updatable = false, length = 17)
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
    @Column(name = "\"year\"", nullable = false)
    private Integer year;

    @Column(name = "model_year")
    private Integer modelYear;

    @Column(name = "production_year")
    private Integer productionYear;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private VehicleStatus status;

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
    private Set<ExtraOption> extraOptions = new HashSet<>();

    @PostLoad
    private void normalizeYear() {
        if (year == null) {
            year = productionYear != null ? productionYear : modelYear;
        }
    }

    @PrePersist
    @PreUpdate
    private void syncLegacyYearColumns() {
        modelYear = year;
        productionYear = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vin != null && vin.equals(vehicle.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }
}
