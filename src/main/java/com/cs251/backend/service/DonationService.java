package com.cs251.backend.service;

import com.cs251.backend.dto.request.DonationRequest;
import com.cs251.backend.entity.Donation;
import com.cs251.backend.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;

    // ── Function 16: บันทึกรับบริจาค ─────────────────────────────────────────
    public Integer record(DonationRequest req) {
        Donation donation = Donation.builder()
                .donationDate(req.getDonationDate())
                .volume(req.getVolume())
                .donorId(req.getDonorId())
                .employeeId(req.getEmployeeId())
                .build();
        return donationRepository.save(donation);
    }
}
