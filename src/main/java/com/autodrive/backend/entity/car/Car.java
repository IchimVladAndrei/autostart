package com.autodrive.backend.entity.car;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 17, max = 17)
    @Column(nullable = false, unique = true, length = 17)
    private String vin;

    @NotBlank
    @Column(nullable = false)
    private String model;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @JsonIgnoreProperties({"cars"})
    private Brand brand;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "car_extra_options",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "extra_option_id")
    )
    @JsonIgnoreProperties({"cars"})
    private List<ExtraOption> extraOptions = new ArrayList<>();
}

