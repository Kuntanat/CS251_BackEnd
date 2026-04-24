package com.cs251.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/** Function 9 & 10: เพิ่ม/แก้ไขผู้ป่วย */
@Data
public class PatientRequest {

    @NotBlank @Size(min = 13, max = 13) private String nationalId;
    @NotBlank private String name;
    @NotBlank @Pattern(regexp = "[MF]") private String gender;
    @NotBlank private String bloodGroup;
    @NotBlank @Pattern(regexp = "[+-]") private String rhFactor;
    @NotNull  private LocalDate birthday;
    private String transfusionStatus;
}
