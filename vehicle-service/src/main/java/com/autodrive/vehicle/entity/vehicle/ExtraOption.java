package com.autodrive.vehicle.entity.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Table(name = "extra_options", schema = "vehicle")
public class ExtraOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @JsonIgnore
    @Builder.Default
    @ManyToMany(mappedBy = "extraOptions", fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();
}

