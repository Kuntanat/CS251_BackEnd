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
public class Deferral {
    private Integer deferralId;
    private String deferralType;
    private String reasonCategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer donorId;
    private Integer employeeId;
}
