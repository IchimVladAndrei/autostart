package com.autodrive.backend.dto.user;

import com.autodrive.backend.entity.user.CustomerStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(String name,

                                    @Size(min = 13, max = 13, message = "CNP must be exactly 13 characters")
                                    String cnp,

                                    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
                                    @Pattern(regexp = "^[0-9+()\\s-]+$", message = "Phone number format invalid")
                                    String phone,

                                    CustomerStatus status
) {
}
