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
public class Donor {
    private Integer donorId;
    private String name;
    private String nationalId;
    private String gender;
    private LocalDate birthday;
    private Integer status;
    private String remark;
    private String bloodGroup;
    private String rhFactor;
    private String congenitalDisease;
}
