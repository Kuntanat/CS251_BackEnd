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
public class Patient {
    private Integer patientId;
    private String nationalId;
    private String name;
    private String gender;
    private String bloodGroup;
    private String rhFactor;
    private LocalDate birthday;
    private String transfusionStatus;
}
