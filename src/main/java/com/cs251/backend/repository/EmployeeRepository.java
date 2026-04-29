package com.cs251.backend.repository;

import com.cs251.backend.dto.request.EmployeeRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;

/**
 * EmployeeRepository — CALL sp_register_employee
 */
@Repository
@RequiredArgsConstructor
public class EmployeeRepository {

    private final JdbcTemplate jdbc;

    // ── Helper: lookup employee name ─────────────────────────────────────────
    public String findNameById(Integer employeeId) {
        try {
            return jdbc.queryForObject(
                "SELECT Name FROM Employee WHERE EmployeeID = ?", String.class, employeeId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // ── Function 2: sp_register_employee ─────────────────────────────────────
    public Integer register(EmployeeRegisterRequest req, String hashedPassword) {
        return jdbc.execute((ConnectionCallback<Integer>) con -> {
            try (var cs = con.prepareCall("{CALL sp_register_employee(?,?,?,?,?,?,?,?)}")) {
                cs.setString(1, req.getName());
                cs.setString(2, req.getRole());
                cs.setDate(3,   Date.valueOf(req.getBirthday()));
                cs.setString(4, req.getEmail());
                cs.setString(5, req.getPhone());
                cs.setString(6, req.getUsername());
                cs.setString(7, hashedPassword);
                cs.registerOutParameter(8, Types.INTEGER);   // OUT p_employeeId
                cs.execute();
                return cs.getInt(8);
            }
        });
    }
}
