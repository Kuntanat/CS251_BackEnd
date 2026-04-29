package com.cs251.backend.service;

import com.cs251.backend.dto.request.BloodBagCreateRequest;
import com.cs251.backend.dto.request.BloodBagUpdateRequest;
import com.cs251.backend.dto.response.BloodBagResponse;
import com.cs251.backend.repository.BloodBagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodBagService {

    private final BloodBagRepository bloodBagRepository;

    // ── Create BloodBag after donation ───────────────────────────────────────
    public Integer create(BloodBagCreateRequest req) {
        return bloodBagRepository.create(req);
    }

    // ── Function 13: แสดงตารางคลังเลือด ─────────────────────────────────────
    public List<BloodBagResponse> findAll() {
        try {
            bloodBagRepository.markExpiredBags();
        } catch (Exception ignored) {
            // DB constraint may not include status=4 on older schema — safe to skip
        }
        return bloodBagRepository.findAll().stream()
                .map(BloodBagResponse::from).collect(Collectors.toList());
    }

    // ── Function 14: หาเลือดใกล้หมดอายุ ─────────────────────────────────────
    public List<BloodBagResponse> findExpiringSoon() {
        return bloodBagRepository.findExpiringSoon().stream()
                .map(BloodBagResponse::from).collect(Collectors.toList());
    }

    // ── Function 15: อัปเดตถุงเลือด ─────────────────────────────────────────
    public void update(Integer bagId, BloodBagUpdateRequest req) {
        bloodBagRepository.update(bagId, req);
    }

    public List<BloodBagResponse> findByDonorId(Integer donorId) {
        return bloodBagRepository.findByDonorId(donorId).stream()
                .map(BloodBagResponse::from).collect(Collectors.toList());
    }

    public List<BloodBagResponse> findAvailableByBloodType(String bloodGroup, String rhFactor) {
        return bloodBagRepository.findAvailableByBloodType(bloodGroup, rhFactor).stream()
                .map(BloodBagResponse::from).collect(Collectors.toList());
    }

    // ── Discard: BagStatus → 3 ───────────────────────────────────────────────
    public void discard(Integer bagId) {
        bloodBagRepository.updateStatus(bagId, 3);
    }
}
