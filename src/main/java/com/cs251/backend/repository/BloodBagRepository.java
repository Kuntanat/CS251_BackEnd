package com.cs251.backend.repository;

import com.cs251.backend.dto.request.BloodBagUpdateRequest;
import com.cs251.backend.entity.BloodBag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * BloodBagRepository — CALL stored procedures
 */
@Repository
@RequiredArgsConstructor
public class BloodBagRepository {

    private final JdbcTemplate jdbc;

    private static final RowMapper<BloodBag> BAG_MAPPER = (rs, rn) -> BloodBag.builder()
            .bagId(rs.getInt("BagID"))
            .componentType(rs.getString("ComponentType"))
            .bloodGroup(rs.getString("BloodGroup"))
            .rhFactor(rs.getString("RhFactor"))
            .collectionDate(rs.getDate("CollectionDate") != null ? rs.getDate("CollectionDate").toLocalDate() : null)
            .expiryDate(rs.getDate("ExpiryDate") != null ? rs.getDate("ExpiryDate").toLocalDate() : null)
            .bagStatus(rs.getInt("BagStatus"))
            .donationId(rs.getInt("DonationID"))
            .build();

    // ── Function 13: sp_list_blood_inventory ─────────────────────────────────
    public List<BloodBag> findAll() {
        return jdbc.query("CALL sp_list_blood_inventory()", BAG_MAPPER);
    }

    // ── Function 14: sp_find_expiring_blood ──────────────────────────────────
    public List<BloodBag> findExpiringSoon() {
        return jdbc.query("CALL sp_find_expiring_blood()", BAG_MAPPER);
    }

    // ── Function 15: sp_update_blood_bag ─────────────────────────────────────
    public void update(Integer bagId, BloodBagUpdateRequest req) {
        jdbc.update("CALL sp_update_blood_bag(?,?,?,?,?,?)",
                bagId,
                req.getDonationId(),
                req.getBloodGroup(),
                req.getRhFactor(),
                Date.valueOf(req.getCollectionDate()),
                Date.valueOf(req.getExpiryDate())
        );
    }

    // ── Function 21: sp_dashboard_volume_in ──────────────────────────────────
    public Integer getTotalVolumeIn(int month, int year) {
        return jdbc.queryForObject("CALL sp_dashboard_volume_in(?,?)", Integer.class, month, year);
    }

    // ── Function 23: sp_dashboard_volume_lost ────────────────────────────────
    public Integer getTotalVolumeLost(int month, int year) {
        return jdbc.queryForObject("CALL sp_dashboard_volume_lost(?,?)", Integer.class, month, year);
    }
}
