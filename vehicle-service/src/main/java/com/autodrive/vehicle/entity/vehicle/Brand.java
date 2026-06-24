package com.autodrive.vehicle.entity.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "brands", schema = "vehicle")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    @Column(length = 100)
    private String country;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();
}
