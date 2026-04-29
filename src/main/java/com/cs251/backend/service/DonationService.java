package com.cs251.backend.service;

import com.cs251.backend.dto.request.DonationRequest;
import com.cs251.backend.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;

    // ── Function 16: บันทึกรับบริจาค ─────────────────────────────────────────
    public Integer record(DonationRequest req) {
        return donationRepository.save(req);
    }
}
