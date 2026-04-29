package com.cs251.backend.service;

import com.cs251.backend.dto.request.BloodTestRequest;
import com.cs251.backend.repository.BloodBagRepository;
import com.cs251.backend.repository.BloodTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BloodTestService {

    private final BloodTestRepository bloodTestRepository;
    private final BloodBagRepository bloodBagRepository;

    // ── Function 17: บันทึกผลทางห้องปฏิบัติการ (TRANSACTION) ─────────────────
    @Transactional
    public Integer record(BloodTestRequest req) {
        Integer testId = bloodTestRepository.save(req);
        // Update ALL bags from the same donation — one test result covers the whole session
        Integer donationId = bloodBagRepository.getDonationIdByBagId(req.getBagId());
        if ("Negative All".equals(req.getInfectiousDiseaseResult())) {
            bloodBagRepository.updateStatusByDonationId(donationId, 0); // Available
        } else {
            bloodBagRepository.updateStatusByDonationId(donationId, 3); // Discard (reactive)
        }
        return testId;
    }
}
