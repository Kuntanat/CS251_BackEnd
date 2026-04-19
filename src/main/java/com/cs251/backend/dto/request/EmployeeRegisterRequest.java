package com.cs251.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/** Function 2: ลงทะเบียน Employee */
@Data
public class EmployeeRegisterRequest {

    @NotBlank private String name;
    @NotBlank private String role;
    @NotNull  private LocalDate birthday;

    // Contact
    @NotBlank @Email private String email;
    @NotBlank        private String phone;

    // Account
    @NotBlank @Size(min = 3, max = 50) private String username;
    @NotBlank @Size(min = 6)           private String password;
}
