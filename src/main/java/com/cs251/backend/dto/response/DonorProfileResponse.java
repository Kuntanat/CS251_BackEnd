package com.cs251.backend.dto.response;

import com.cs251.backend.entity.Donor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorProfileResponse {
    private Integer donorId;
    private String nationalId;
    private String name;
    private String gender;
    private LocalDate birthday;
    private String bloodGroup;
    private String rhFactor;
    private String congenitalDisease;
    private Integer status;
    private String remark;

    private String email;
    private String phone;
    private String place;

    @SuppressWarnings("unchecked")
    public static DonorProfileResponse from(Map<String, Object> payload) {
        Donor donor = (Donor) payload.get("donor");
        List<Map<String, Object>> contacts = (List<Map<String, Object>>) payload.get("contacts");

        String email = null;
        String phone = null;
        String place = null;
        if (contacts != null) {
            for (Map<String, Object> c : contacts) {
                String type = String.valueOf(c.get("ContactType"));
                String value = String.valueOf(c.get("ContactValue"));
                if ("Email".equalsIgnoreCase(type)) email = value;
                else if ("Phone".equalsIgnoreCase(type)) phone = value;
                else if ("Place".equalsIgnoreCase(type)) place = value;
            }
        }

        return DonorProfileResponse.builder()
                .donorId(donor.getDonorId())
                .nationalId(donor.getNationalId())
                .name(donor.getName())
                .gender(donor.getGender())
                .birthday(donor.getBirthday())
                .bloodGroup(donor.getBloodGroup())
                .rhFactor(donor.getRhFactor())
                .congenitalDisease(donor.getCongenitalDisease())
                .status(donor.getStatus())
                .remark(donor.getRemark())
                .email(email)
                .phone(phone)
                .place(place)
                .build();
    }
}

