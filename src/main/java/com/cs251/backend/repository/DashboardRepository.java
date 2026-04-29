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

    // ── Daily usage volume for chart outbound line (ml) ──────────────────────
    public List<Map<String, Object>> getUsageReport(int month, int year) {
        return jdbc.queryForList("""
                SELECT DATE(bu.UsageDate) AS usageDate,
                       COALESCE(SUM(dn.Volume), 0) AS totalUsed
                FROM BloodUsage bu
                JOIN BloodBag bb ON bb.BagID  = bu.BagID
                JOIN Donation dn  ON dn.DonationID = bb.DonationID
                WHERE MONTH(bu.UsageDate) = ? AND YEAR(bu.UsageDate) = ?
                GROUP BY DATE(bu.UsageDate)
                ORDER BY DATE(bu.UsageDate)
                """, month, year);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new java.util.LinkedHashMap<>();
        stats.put("activeDonors",   jdbc.queryForObject("SELECT COUNT(*) FROM Donor WHERE Status = 1", Integer.class));
        stats.put("availableBags",  jdbc.queryForObject("SELECT COUNT(*) FROM BloodBag WHERE BagStatus = 0", Integer.class));
        stats.put("expiringSoon",   jdbc.queryForObject(
                "SELECT COUNT(*) FROM BloodBag WHERE BagStatus = 0 AND ExpiryDate <= DATE_ADD(CURDATE(), INTERVAL 7 DAY)", Integer.class));
        stats.put("dispensedToday", jdbc.queryForObject(
                "SELECT COUNT(*) FROM BloodUsage WHERE UsageDate = CURDATE()", Integer.class));
        return stats;
    }

    public List<Map<String, Object>> getBloodStock() {
        return jdbc.queryForList(
                "SELECT BloodGroup, RhFactor, COUNT(*) AS units " +
                "FROM BloodBag WHERE BagStatus = 0 " +
                "GROUP BY BloodGroup, RhFactor ORDER BY BloodGroup, RhFactor");
    }
}
