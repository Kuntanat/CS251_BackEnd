package com.cs251.backend.service;

import com.cs251.backend.dto.request.BloodUsageRequest;
import com.cs251.backend.repository.BloodUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BloodUsageService {

    private final BloodUsageRepository bloodUsageRepository;

    // ── Function 18 + 19 ──────────────────────────────────────────────────────
    // Trigger after_blood_usage_insert จะอัปเดต BagStatus → 2 อัตโนมัติ
    public Integer record(BloodUsageRequest req) {
        return bloodUsageRepository.save(req);
    }
}
