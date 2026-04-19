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
public class Donation {
    private Integer donationId;
    private LocalDate donationDate;
    private LocalDate nextEligibleDate;
    private Integer volume;
    private Integer donorId;
    private Integer employeeId;
}
