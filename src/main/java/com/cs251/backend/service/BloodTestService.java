package com.cs251.backend.service;

import com.cs251.backend.dto.request.BloodTestRequest;
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
        // sp_record_blood_test จัดการ insert + update BagStatus (ถ้า Negative All) ภายใน procedure แล้ว
        return bloodTestRepository.save(req);
    }
}
