package com.cs251.backend.dto.response;

import com.cs251.backend.entity.Donor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DonorResponse {
    private Integer donorId;
    private String name;
    private String bloodGroup;
    private String rhFactor;
    private Integer status;
    private String remark;

    public static DonorResponse from(Donor d) {
        return DonorResponse.builder()
                .donorId(d.getDonorId())
                .name(d.getName())
                .bloodGroup(d.getBloodGroup())
                .rhFactor(d.getRhFactor())
                .status(d.getStatus())
                .remark(d.getRemark())
                .build();
    }
}
