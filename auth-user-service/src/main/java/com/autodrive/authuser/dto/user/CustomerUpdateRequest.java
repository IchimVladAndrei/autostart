package com.autodrive.authuser.dto.user;

import com.autodrive.authuser.entity.user.CustomerStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(String name,

                                    @Pattern(regexp = "^\\d{13}$", message = "CNP must contain exactly 13 digits")
                                    String cnp,

                                    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
                                    @Pattern(regexp = "^[0-9+()\\s-]+$", message = "Phone number format invalid")
                                    String phone,

                                    CustomerStatus status
) {
}
