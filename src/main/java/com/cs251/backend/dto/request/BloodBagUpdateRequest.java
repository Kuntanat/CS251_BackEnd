package com.cs251.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/** Function 15: อัปเดตถุงเลือด */
@Data
public class BloodBagUpdateRequest {

    @NotNull private Integer donationId;
    @NotNull private String bloodGroup;
    @NotNull private String rhFactor;
    @NotNull private LocalDate collectionDate;
    @NotNull private LocalDate expiryDate;
}
