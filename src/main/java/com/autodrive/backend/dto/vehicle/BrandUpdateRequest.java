package com.autodrive.backend.dto.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BrandUpdateRequest(@Size(min = 2, max = 100)
                                 @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
                                 String name,
                                 @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
                                 @Pattern(regexp = ".*\\S.*", message = "Value must not be blank")
                                 String country,
                                 @Min(value = 1800, message = "Founded year must be greater than or equal to 1800")
                                 Integer foundedYear) {

}
