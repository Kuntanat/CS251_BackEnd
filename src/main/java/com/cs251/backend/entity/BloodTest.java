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
public class BloodTest {
    private Integer testId;
    private String infectiousDiseaseResult;
    private String confirmatoryAbo;
    private String confirmatoryRh;
    private LocalDate testDate;
    private Integer bagId;
}
