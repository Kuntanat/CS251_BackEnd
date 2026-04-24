package com.cs251.backend.dto.request;

import lombok.Data;

import java.time.LocalDate;

/** Function 6: แก้ไขข้อมูล Donor */
@Data
public class UpdateDonorRequest {
    private String name;
    private LocalDate birthday;
    private String congenitalDisease;
    private String phone;
    private String email;
    private String place;
}
