package com.cs251.backend.dto.response;

import com.cs251.backend.entity.BloodBag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BloodBagResponse {
    private Integer bagId;
    private Integer donationId;
    private String componentType;
    private String bloodGroup;
    private String rhFactor;
    private LocalDate collectionDate;
    private LocalDate expiryDate;
    private Integer bagStatus;

    public static BloodBagResponse from(BloodBag b) {
        return BloodBagResponse.builder()
                .bagId(b.getBagId())
                .donationId(b.getDonationId())
                .componentType(b.getComponentType())
                .bloodGroup(b.getBloodGroup())
                .rhFactor(b.getRhFactor())
                .collectionDate(b.getCollectionDate())
                .expiryDate(b.getExpiryDate())
                .bagStatus(b.getBagStatus())
                .build();
    }
}
