package com.cs251.backend.dto.response;

import com.cs251.backend.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientResponse {
    private Integer patientId;
    private String nationalId;
    private String name;
    private String gender;
    private String bloodGroup;
    private String rhFactor;
    private LocalDate birthday;
    private String transfusionStatus;

    public static PatientResponse from(Patient p) {
        return PatientResponse.builder()
                .patientId(p.getPatientId())
                .nationalId(p.getNationalId())
                .name(p.getName())
                .gender(p.getGender())
                .bloodGroup(p.getBloodGroup())
                .rhFactor(p.getRhFactor())
                .birthday(p.getBirthday())
                .transfusionStatus(p.getTransfusionStatus())
                .build();
    }
}
