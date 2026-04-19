package com.cs251.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodUsage {
    private Integer usageId;
    private LocalDate usageDate;
    private Integer patientId;
    private Integer bagId;
    private Integer employeeId;
}
