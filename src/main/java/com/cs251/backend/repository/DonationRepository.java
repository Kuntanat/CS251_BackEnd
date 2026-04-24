package com.cs251.backend.repository;

import com.cs251.backend.dto.request.DonationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;

/**
 * DonationRepository — CALL sp_record_donation
 */
@Repository
@RequiredArgsConstructor
public class DonationRepository {

    private final JdbcTemplate jdbc;

    // ── Function 16: sp_record_donation ──────────────────────────────────────
    public Integer save(DonationRequest req) {
        return jdbc.execute((java.sql.ConnectionCallback<Integer>) con -> {
            try (var cs = con.prepareCall("{CALL sp_record_donation(?,?,?,?,?)}")) {
                cs.setDate(1, Date.valueOf(req.getDonationDate()));
                cs.setInt(2,  req.getVolume());
                cs.setInt(3,  req.getDonorId());
                cs.setInt(4,  req.getEmployeeId());
                cs.registerOutParameter(5, Types.INTEGER);   // OUT p_donationId
                cs.execute();
                return cs.getInt(5);
            }
        });
    }
}
