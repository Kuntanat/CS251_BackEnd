package com.cs251.backend.service;

import com.cs251.backend.dto.request.DonorRegisterRequest;
import com.cs251.backend.dto.request.UpdateDonorRequest;
import com.cs251.backend.dto.response.DonorDashboardResponse;
import com.cs251.backend.dto.response.DonorProfileResponse;
import com.cs251.backend.dto.response.DonorDonationHistoryResponse;
import com.cs251.backend.dto.response.DonorResponse;
import com.cs251.backend.repository.DonationReadRepository;
import com.cs251.backend.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;
    private final DonationReadRepository donationReadRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Function 1 ────────────────────────────────────────────────────────────
    public Integer register(DonorRegisterRequest req) {
        String hashed = passwordEncoder.encode(req.getPassword());
        Integer id = donorRepository.register(req, hashed);
        log.info("Donor registered: id={}", id);
        return id;
    }

    // ── Function 4 ────────────────────────────────────────────────────────────
    public List<DonorResponse> findAll() {
        return donorRepository.findAll().stream()
                .map(DonorResponse::from).collect(Collectors.toList());
    }

    // ── Function 6 ────────────────────────────────────────────────────────────
    public void update(Integer donorId, UpdateDonorRequest req) {
        donorRepository.update(donorId, req);
    }

    // ── Function 7 ────────────────────────────────────────────────────────────
    public void suspend(Integer donorId, String remark) {
        donorRepository.suspend(donorId, remark);
        log.info("Donor {} suspended", donorId);
    }

    // ── Function 8 ────────────────────────────────────────────────────────────
    public void reinstate(Integer donorId, String remark) {
        donorRepository.reinstate(donorId, remark);
        log.info("Donor {} reinstated", donorId);
    }

    // ── Function 11 ───────────────────────────────────────────────────────────
    public List<DonorResponse> search(String name, Integer donorId) {
        return donorRepository.search(name, donorId).stream()
                .map(DonorResponse::from).collect(Collectors.toList());
    }

    // ── Donor Self: Profile ──────────────────────────────────────────────────
    public DonorProfileResponse getProfile(Integer donorId) {
        Map<String, Object> payload = donorRepository.getProfile(donorId);
        return DonorProfileResponse.from(payload);
    }

    // ── Donor Self: Donation history ─────────────────────────────────────────
    public List<DonorDonationHistoryResponse> getDonationHistory(Integer donorId) {
        return donationReadRepository.findHistoryByDonorId(donorId);
    }

    // ── Donor Self: Dashboard summary ────────────────────────────────────────
    public DonorDashboardResponse getDashboard(Integer donorId) {
        Map<String, Object> row = donorRepository.getDashboardSummary(donorId);

        LocalDate nextEligible = (LocalDate) row.get("nextEligibleDate");
        Integer donorStatus = ((Number) row.get("donorStatus")).intValue();

        boolean ready = donorStatus == 1 && (nextEligible == null || !nextEligible.isAfter(LocalDate.now()));

        return DonorDashboardResponse.builder()
                .donorId(((Number) row.get("donorId")).intValue())
                .latestDonationDate((LocalDate) row.get("latestDonationDate"))
                .nextEligibleDate(nextEligible)
                .readyToDonate(ready)
                .totalDonations(row.get("totalDonations") == null ? 0 : ((Number) row.get("totalDonations")).intValue())
                .build();
    }
}
