package com.autodrive.backend.entity.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();
}
