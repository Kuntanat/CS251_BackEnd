package com.cs251.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/** Function 16: บันทึกรับบริจาค */
@Data
public class DonationRequest {
    @NotNull private LocalDate donationDate;
    @NotNull private Integer volume;
    @NotNull private Integer donorId;
    @NotNull private Integer employeeId;
}
