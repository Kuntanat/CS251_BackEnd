package com.cs251.backend.repository;

import com.cs251.backend.dto.request.BloodUsageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;

/**
 * BloodUsageRepository — CALL stored procedures
 */
@Repository
@RequiredArgsConstructor
public class BloodUsageRepository {

    private final JdbcTemplate jdbc;

    // ── Function 18: sp_record_blood_usage ───────────────────────────────────
    // Trigger after_blood_usage_insert จะ handle Function 19 อัตโนมัติ
    public Integer save(BloodUsageRequest req) {
        return jdbc.execute((ConnectionCallback<Integer>) con -> {
            try (var cs = con.prepareCall("{CALL sp_record_blood_usage(?,?,?,?,?)}")) {
                cs.setDate(1, Date.valueOf(req.getUsageDate()));
                cs.setInt(2,  req.getPatientId());
                cs.setInt(3,  req.getBagId());
                cs.setInt(4,  req.getEmployeeId());
                cs.registerOutParameter(5, Types.INTEGER);   // OUT p_usageId
                cs.execute();
                return cs.getInt(5);
            }
        });
    }

    // ── Function 22: sp_dashboard_volume_used ────────────────────────────────
    public Integer getTotalVolumeUsed(int month, int year) {
        return jdbc.queryForObject("CALL sp_dashboard_volume_used(?,?)", Integer.class, month, year);
    }
}
