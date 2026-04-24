package com.cs251.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/** Function 20: บันทึกการระงับ */
@Data
public class DeferralRequest {
    @NotBlank private String deferralType;
    @NotBlank private String reasonCategory;
    @NotNull  private LocalDate startDate;
    @NotNull  private Integer donorId;
    @NotNull  private Integer employeeId;
}
