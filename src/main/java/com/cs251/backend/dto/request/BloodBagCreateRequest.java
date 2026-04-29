package com.cs251.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BloodBagCreateRequest {
    @NotBlank  private String componentType;
    @NotBlank  private String bloodGroup;
    @NotBlank  private String rhFactor;
    @NotNull   private LocalDate collectionDate;
    @NotNull   private LocalDate expiryDate;
    @NotNull   private Integer donationId;
}
