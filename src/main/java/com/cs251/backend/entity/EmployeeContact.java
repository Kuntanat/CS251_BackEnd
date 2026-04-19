package com.cs251.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeContact {
    private Integer employeeContactId;
    private String contactType;
    private String contactValue;
    private Integer employeeId;
}
