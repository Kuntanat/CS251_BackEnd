package com.cs251.backend.repository;

import com.cs251.backend.dto.request.BloodTestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;

/**
 * BloodTestRepository — CALL sp_record_blood_test
 */
@Repository
@RequiredArgsConstructor
public class BloodTestRepository {

    private final JdbcTemplate jdbc;

    // ── Function 17: sp_record_blood_test ────────────────────────────────────
    public Integer save(BloodTestRequest req) {
        return jdbc.execute((java.sql.ConnectionCallback<Integer>) con -> {
            try (var cs = con.prepareCall("{CALL sp_record_blood_test(?,?,?,?,?,?)}")) {
                cs.setString(1, req.getInfectiousDiseaseResult());
                cs.setString(2, req.getConfirmatoryAbo());
                cs.setString(3, req.getConfirmatoryRh());
                cs.setDate(4,   Date.valueOf(req.getTestDate()));
                cs.setInt(5,    req.getBagId());
                cs.registerOutParameter(6, Types.INTEGER);   // OUT p_testId
                cs.execute();
                return cs.getInt(6);
            }
        });
    }
}
