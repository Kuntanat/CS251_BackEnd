package com.cs251.backend.service;

import com.cs251.backend.dto.response.DashboardResponse;
import com.cs251.backend.repository.BloodBagRepository;
import com.cs251.backend.repository.BloodUsageRepository;
import com.cs251.backend.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BloodBagRepository bloodBagRepository;
    private final BloodUsageRepository bloodUsageRepository;
    private final DashboardRepository dashboardRepository;

    // ── Function 21, 22, 23: Dashboard สรุปปริมาณเลือดในเดือน ────────────────
    public DashboardResponse getSummary(int month, int year) {
        int totalIn   = bloodBagRepository.getTotalVolumeIn(month, year);
        int totalUsed = bloodUsageRepository.getTotalVolumeUsed(month, year);
        int totalLost = bloodBagRepository.getTotalVolumeLost(month, year);

        return DashboardResponse.builder()
                .month(month)
                .year(year)
                .totalVolumeIn(totalIn)
                .totalVolumeUsed(totalUsed)
                .totalVolumeLost(totalLost)
                .build();
    }

    // ── Function 24: ตารางรายงานการรับบริจาค ─────────────────────────────────
    public List<Map<String, Object>> getDonationReport(int month, int year) {
        return dashboardRepository.getDonationReport(month, year);
    }
}
