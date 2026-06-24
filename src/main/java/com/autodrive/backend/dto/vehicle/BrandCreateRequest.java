package com.autodrive.backend.dto.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BrandCreateRequest(@NotBlank @Size(min = 2, max = 100) String name,
                                 @NotBlank(message = "Country is required")
                                 @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
                                 String country,

                                 @NotNull(message = "Founded year is required")
                                 @Min(value = 1800, message = "Founded year must be greater than or equal to 1800")
                                 Integer foundedYear) {


}
