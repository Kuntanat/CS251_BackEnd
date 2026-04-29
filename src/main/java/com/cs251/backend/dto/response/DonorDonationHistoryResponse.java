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
public class DonorDonationHistoryResponse {
    private LocalDate donationDate;
    private String donationType;      // Whole Blood / Platelets / PRC / ...
    private Integer volume;           // ml
    private String screeningResult;   // ผ่าน / ไม่ผ่าน
}

