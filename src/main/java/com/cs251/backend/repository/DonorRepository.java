package com.cs251.backend.repository;

import com.cs251.backend.dto.request.DonorRegisterRequest;
import com.cs251.backend.dto.request.UpdateDonorRequest;
import com.cs251.backend.entity.Donor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DonorRepository — DAO ที่ CALL Stored Procedure ล้วนๆ ไม่มี SQL ใน Java
 */
@Repository
@RequiredArgsConstructor
public class DonorRepository {

    private final JdbcTemplate jdbc;

    static final RowMapper<Donor> DONOR_MAPPER = (rs, rn) -> Donor.builder()
            .donorId(rs.getInt("DonorID"))
            .name(rs.getString("Name"))
            .nationalId(rs.getString("NationalID"))
            .gender(rs.getString("Gender"))
            .birthday(rs.getDate("Birthday") != null ? rs.getDate("Birthday").toLocalDate() : null)
            .status(rs.getInt("Status"))
            .remark(rs.getString("Remark"))
            .bloodGroup(rs.getString("BloodGroup"))
            .rhFactor(rs.getString("RhFactor"))
            .congenitalDisease(rs.getString("CongenitalDisease"))
            .build();

    // ── Function 1: sp_register_donor ────────────────────────────────────────
    public Integer register(DonorRegisterRequest req, String hashedPassword) {
        return jdbc.execute((ConnectionCallback<Integer>) con -> {
            try (var cs = con.prepareCall("{CALL sp_register_donor(?,?,?,?,?,?,?,?,?,?,?,?,?)}")) {
                cs.setString(1,  req.getName());
                cs.setString(2,  req.getNationalId());
                cs.setString(3,  req.getGender());
                cs.setDate(4,    Date.valueOf(req.getBirthday()));
                cs.setString(5,  req.getBloodGroup());
                cs.setString(6,  req.getRhFactor());
                cs.setString(7,  req.getCongenitalDisease());
                cs.setString(8,  req.getEmail());
                cs.setString(9,  req.getPhone());
                cs.setString(10, req.getPlace());
                cs.setString(11, req.getUsername());
                cs.setString(12, hashedPassword);
                cs.registerOutParameter(13, Types.INTEGER);   // OUT p_donorId
                cs.execute();
                return cs.getInt(13);
            }
        });
    }

    // ── Function 4: sp_list_donors ───────────────────────────────────────────
    public List<Donor> findAll() {
        return jdbc.query("CALL sp_list_donors()", DONOR_MAPPER);
    }

    // ── Function 6: sp_update_donor ──────────────────────────────────────────
    public void update(Integer donorId, UpdateDonorRequest req) {
        jdbc.update("CALL sp_update_donor(?,?,?,?,?,?,?)",
                donorId,
                req.getName(),
                req.getBirthday() != null ? Date.valueOf(req.getBirthday()) : null,
                req.getCongenitalDisease(),
                req.getPhone(),
                req.getEmail(),
                req.getPlace()
        );
    }

    // ── Function 7: sp_suspend_donor ─────────────────────────────────────────
    public void suspend(Integer donorId, String remark) {
        jdbc.update("CALL sp_suspend_donor(?,?)", donorId, remark);
    }

    // ── Function 8: sp_reinstate_donor ───────────────────────────────────────
    public void reinstate(Integer donorId, String remark) {
        jdbc.update("CALL sp_reinstate_donor(?,?)", donorId, remark);
    }

    // ── Function 11: sp_search_donor ─────────────────────────────────────────
    public List<Donor> search(String name, Integer donorId) {
        return jdbc.query("CALL sp_search_donor(?,?)",
                DONOR_MAPPER,
                name != null ? name : "",
                donorId != null ? donorId : -1
        );
    }

    // ── Helper: sp_find_donor_by_id ──────────────────────────────────────────
    public Optional<Donor> findById(Integer donorId) {
        try {
            return Optional.ofNullable(
                jdbc.queryForObject("CALL sp_find_donor_by_id(?)", DONOR_MAPPER, donorId)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // ── Donor Self: Profile (Donor + DonorContact) ───────────────────────────
    public Map<String, Object> getProfile(Integer donorId) {
        // ใช้ queryForList เพื่อดึง contact หลายรายการโดยไม่ต้องสร้าง entity เพิ่ม
        var donor = jdbc.queryForObject("CALL sp_find_donor_by_id(?)", DONOR_MAPPER, donorId);
        if (donor == null) {
            throw new IllegalArgumentException("Donor not found: " + donorId);
        }

        List<Map<String, Object>> contacts = jdbc.queryForList(
                "SELECT ContactType, ContactValue FROM DonorContact WHERE DonorID = ?",
                donorId
        );

        return Map.of(
                "donor", donor,
                "contacts", contacts
        );
    }

    // ── Donor Self: Dashboard summary ────────────────────────────────────────
    public Map<String, Object> getDashboardSummary(Integer donorId) {
        // รวมข้อมูลหลักที่ Figma ต้องใช้: latest donation, next eligible, total donation count
        return jdbc.queryForMap(
                """
                SELECT
                    d.DonorID AS donorId,
                    d.Status AS donorStatus,
                    MAX(dn.DonationDate) AS latestDonationDate,
                    MAX(dn.NextEligibleDate) AS nextEligibleDate,
                    SUM(CASE WHEN dn.Volume > 0 THEN 1 ELSE 0 END) AS totalDonations
                FROM Donor d
                LEFT JOIN Donation dn ON dn.DonorID = d.DonorID
                WHERE d.DonorID = ?
                GROUP BY d.DonorID, d.Status
                """,
                donorId
        );
    }
}
