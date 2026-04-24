package com.cs251.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/** Function 1: ลงทะเบียน Donor */
@Data
public class DonorRegisterRequest {

    @NotBlank private String name;
    @NotBlank @Size(min = 13, max = 13) private String nationalId;
    @NotBlank @Pattern(regexp = "[MF]") private String gender;
    @NotNull  private LocalDate birthday;
    @NotBlank private String bloodGroup;
    @NotBlank @Pattern(regexp = "[+-]") private String rhFactor;
    private String congenitalDisease;

    // Contact
    @NotBlank @Email private String email;
    @NotBlank        private String phone;
    private String place;

    // Account
    @NotBlank @Size(min = 3, max = 50) private String username;
    @NotBlank @Size(min = 6)           private String password;
}
