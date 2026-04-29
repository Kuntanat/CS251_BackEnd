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
        // Explicitly update BagStatus based on result (do not rely on trigger alone)
        if ("Negative All".equals(req.getInfectiousDiseaseResult())) {
            bloodBagRepository.updateStatus(req.getBagId(), 0); // Available
        } else {
            bloodBagRepository.updateStatus(req.getBagId(), 3); // Discard (reactive)
        }
        return testId;
    }
}
