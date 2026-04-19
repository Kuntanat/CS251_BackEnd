package com.cs251.backend.service;

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

    // ── Function 13: แสดงตารางคลังเลือด ─────────────────────────────────────
    public List<BloodBagResponse> findAll() {
        return bloodBagRepository.findAllOrderByExpiry().stream()
                .map(BloodBagResponse::from).collect(Collectors.toList());
    }

    // ── Function 14: หาเลือดใกล้หมดอายุ ─────────────────────────────────────
    public List<BloodBagResponse> findExpiringSoon() {
        return bloodBagRepository.findExpiringSoon().stream()
                .map(BloodBagResponse::from).collect(Collectors.toList());
    }

    // ── Function 15: อัปเดตถุงเลือด ─────────────────────────────────────────
    public void update(Integer bagId, BloodBagUpdateRequest req) {
        bloodBagRepository.update(
                bagId,
                req.getBloodGroup(),
                req.getRhFactor(),
                req.getCollectionDate(),
                req.getExpiryDate(),
                req.getDonationId()
        );
    }
}
