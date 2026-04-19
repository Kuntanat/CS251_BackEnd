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
public class BloodBag {
    private Integer bagId;
    private String componentType;
    private String bloodGroup;
    private String rhFactor;
    private LocalDate collectionDate;
    private LocalDate expiryDate;
    private Integer bagStatus;  // 0=Available, 1=Reserved, 2=Used, 3=Disposed
    private Integer donationId;
}
