package com.cs251.backend.dto.response;

import com.cs251.backend.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientResponse {
    private Integer patientId;
    private String name;
    private String bloodGroup;
    private String rhFactor;
    private String transfusionStatus;

    public static PatientResponse from(Patient p) {
        return PatientResponse.builder()
                .patientId(p.getPatientId())
                .name(p.getName())
                .bloodGroup(p.getBloodGroup())
                .rhFactor(p.getRhFactor())
                .transfusionStatus(p.getTransfusionStatus())
                .build();
    }
}
