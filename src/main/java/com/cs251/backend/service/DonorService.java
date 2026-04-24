package com.cs251.backend.service;

import com.cs251.backend.dto.request.DonorRegisterRequest;
import com.cs251.backend.dto.request.UpdateDonorRequest;
import com.cs251.backend.dto.response.DonorResponse;
import com.cs251.backend.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;
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
}
