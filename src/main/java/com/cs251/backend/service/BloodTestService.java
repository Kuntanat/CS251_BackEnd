package com.cs251.backend.service;

import com.cs251.backend.dto.request.BloodTestRequest;
import com.cs251.backend.entity.BloodTest;
import com.cs251.backend.repository.BloodTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BloodTestService {

    private final BloodTestRepository bloodTestRepository;

    // ── Function 17: บันทึกผลทางห้องปฏิบัติการ (TRANSACTION) ─────────────────
    @Transactional
    public Integer record(BloodTestRequest req) {
        BloodTest test = BloodTest.builder()
                .infectiousDiseaseResult(req.getInfectiousDiseaseResult())
                .confirmatoryAbo(req.getConfirmatoryAbo())
                .confirmatoryRh(req.getConfirmatoryRh())
                .testDate(req.getTestDate())
                .bagId(req.getBagId())
                .build();

        Integer testId = bloodTestRepository.save(test);

        // อัปเดต BagStatus → Available(0) ถ้าผลเป็น Negative All
        bloodTestRepository.updateBagStatusIfNegative(req.getBagId());

        return testId;
    }
}
