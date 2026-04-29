package com.cs251.backend.repository;

import com.cs251.backend.dto.request.DeferralRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;

/**
 * DeferralRepository — CALL sp_record_deferral
 */
@Repository
@RequiredArgsConstructor
public class DeferralRepository {

    private final JdbcTemplate jdbc;

    // ── Function 20: sp_record_deferral ──────────────────────────────────────
    // Procedure อัปเดต Donor.Status → 0 อัตโนมัติอยู่แล้ว
    public Integer save(DeferralRequest req) {
        return jdbc.execute((ConnectionCallback<Integer>) con -> {
            try (var cs = con.prepareCall("{CALL sp_record_deferral(?,?,?,?,?,?)}")) {
                cs.setString(1, req.getDeferralType());
                cs.setString(2, req.getReasonCategory());
                cs.setDate(3,   Date.valueOf(req.getStartDate()));
                cs.setInt(4,    req.getDonorId());
                cs.setInt(5,    req.getEmployeeId());
                cs.registerOutParameter(6, Types.INTEGER);   // OUT p_deferralId
                cs.execute();
                return cs.getInt(6);
            }
        });
    }
}
