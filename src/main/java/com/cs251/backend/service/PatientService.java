package com.cs251.backend.service;

import com.cs251.backend.dto.request.PatientRequest;
import com.cs251.backend.dto.response.PatientResponse;
import com.cs251.backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    // ── Function 5: แสดงตารางผู้ป่วย ─────────────────────────────────────────
    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(PatientResponse::from).collect(Collectors.toList());
    }

    // ── Function 9: เพิ่มผู้ป่วยใหม่ ─────────────────────────────────────────
    public Integer create(PatientRequest req) {
        return patientRepository.save(req);
    }

    // ── Function 10: แก้ไขข้อมูลผู้ป่วย ─────────────────────────────────────
    public void update(Integer patientId, PatientRequest req) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));
        patientRepository.update(patientId, req);
    }

    // ── Function 12: ค้นหาผู้ป่วย ────────────────────────────────────────────
    public List<PatientResponse> search(String name, Integer patientId) {
        return patientRepository.search(name, patientId).stream()
                .map(PatientResponse::from).collect(Collectors.toList());
    }
}
