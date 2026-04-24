package com.cs251.backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * DashboardRepository — CALL sp_donation_report
 */
@Repository
@RequiredArgsConstructor
public class DashboardRepository {

    private final JdbcTemplate jdbc;

    // ── Function 24: sp_donation_report ──────────────────────────────────────
    public List<Map<String, Object>> getDonationReport(int month, int year) {
        return jdbc.queryForList("CALL sp_donation_report(?,?)", month, year);
    }
}
