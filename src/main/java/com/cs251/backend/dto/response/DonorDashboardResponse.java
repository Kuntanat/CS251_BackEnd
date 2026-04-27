package com.cs251.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorDashboardResponse {
    private Integer donorId;
    private LocalDate latestDonationDate;
    private LocalDate nextEligibleDate;
    private boolean readyToDonate;
    private int totalDonations;
}

