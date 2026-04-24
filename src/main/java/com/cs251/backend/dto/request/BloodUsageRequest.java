package com.cs251.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/** Function 18: บันทึกการจ่ายเลือด */
@Data
public class BloodUsageRequest {
    @NotNull private LocalDate usageDate;
    @NotNull private Integer patientId;
    @NotNull private Integer bagId;
    @NotNull private Integer employeeId;
}
