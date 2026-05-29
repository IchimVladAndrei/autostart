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
@Table(name = "extra_options")
public class ExtraOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Option name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @Builder.Default
    @ManyToMany(mappedBy = "extraOptions", fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();
}

