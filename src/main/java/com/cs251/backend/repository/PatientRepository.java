package com.cs251.backend.repository;

import com.cs251.backend.dto.request.PatientRequest;
import com.cs251.backend.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

/**
 * PatientRepository — CALL stored procedures
 */
@Repository
@RequiredArgsConstructor
public class PatientRepository {

    private final JdbcTemplate jdbc;

    private static final RowMapper<Patient> PATIENT_MAPPER = (rs, rn) -> Patient.builder()
            .patientId(rs.getInt("PatientID"))
            .nationalId(rs.getString("NationalID"))
            .name(rs.getString("Name"))
            .gender(rs.getString("Gender"))
            .bloodGroup(rs.getString("BloodGroup"))
            .rhFactor(rs.getString("RhFactor"))
            .birthday(rs.getDate("Birthday") != null ? rs.getDate("Birthday").toLocalDate() : null)
            .transfusionStatus(rs.getString("TransfusionStatus"))
            .build();

    // ── Function 5: sp_list_patients ─────────────────────────────────────────
    public List<Patient> findAll() {
        return jdbc.query("CALL sp_list_patients()", PATIENT_MAPPER);
    }

    // ── Function 9: sp_add_patient ───────────────────────────────────────────
    public Integer save(PatientRequest req) {
        return jdbc.execute((java.sql.ConnectionCallback<Integer>) con -> {
            try (var cs = con.prepareCall("{CALL sp_add_patient(?,?,?,?,?,?,?,?)}")) {
                cs.setString(1, req.getNationalId());
                cs.setString(2, req.getName());
                cs.setString(3, req.getGender());
                cs.setString(4, req.getBloodGroup());
                cs.setString(5, req.getRhFactor());
                cs.setDate(6,   Date.valueOf(req.getBirthday()));
                cs.setString(7, req.getTransfusionStatus());
                cs.registerOutParameter(8, Types.INTEGER);   // OUT p_patientId
                cs.execute();
                return cs.getInt(8);
            }
        });
    }

    // ── Function 10: sp_update_patient ───────────────────────────────────────
    public void update(Integer patientId, PatientRequest req) {
        jdbc.update("CALL sp_update_patient(?,?,?,?,?,?,?,?)",
                patientId,
                req.getNationalId(),
                req.getName(),
                req.getGender(),
                req.getBloodGroup(),
                req.getRhFactor(),
                Date.valueOf(req.getBirthday()),
                req.getTransfusionStatus()
        );
    }

    // ── Function 12: sp_search_patient ───────────────────────────────────────
    public List<Patient> search(String name, Integer patientId) {
        return jdbc.query("CALL sp_search_patient(?,?)",
                PATIENT_MAPPER,
                name != null ? name : "",
                patientId != null ? patientId : -1
        );
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    public Optional<Patient> findById(Integer patientId) {
        try {
            return Optional.ofNullable(
                jdbc.queryForObject("CALL sp_find_patient_by_id(?)", PATIENT_MAPPER, patientId)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
