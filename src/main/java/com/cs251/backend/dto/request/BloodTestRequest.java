package com.cs251.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/** Function 17: บันทึกผลทางห้องปฏิบัติการ */
@Data
public class BloodTestRequest {
    @NotBlank private String infectiousDiseaseResult;
    @NotBlank private String confirmatoryAbo;
    @NotBlank private String confirmatoryRh;
    @NotNull  private LocalDate testDate;
    @NotNull  private Integer bagId;
}
