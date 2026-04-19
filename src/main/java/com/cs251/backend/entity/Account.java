package com.cs251.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Integer accountId;
    private String username;
    private String password;
    private String userType;    // 'Employee' หรือ 'Donor'
    private Integer status;    // 1=ปกติ, 0=ระงับ
    private Integer referenceId; // DonorID หรือ EmployeeID
}
