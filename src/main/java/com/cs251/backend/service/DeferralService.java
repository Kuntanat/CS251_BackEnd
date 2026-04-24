package com.cs251.backend.service;

import com.cs251.backend.dto.request.DeferralRequest;
import com.cs251.backend.repository.DeferralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeferralService {

    private final DeferralRepository deferralRepository;

    // ── Function 20 ───────────────────────────────────────────────────────────
    // sp_record_deferral อัปเดต Donor.Status → 0 อัตโนมัติอยู่ใน procedure แล้ว
    public Integer record(DeferralRequest req) {
        return deferralRepository.save(req);
    }
}
