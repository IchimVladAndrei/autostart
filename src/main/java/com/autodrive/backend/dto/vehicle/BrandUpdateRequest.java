package com.autodrive.backend.dto.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record BrandUpdateRequest(@Size(min = 2, max = 100) String name,
                                 @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
                                 String country,
                                 @Min(value = 1800, message = "Founded year must be greater than or equal to 1800")
                                 Integer foundedYear) {

}
