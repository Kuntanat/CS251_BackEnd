package com.cs251.backend.repository;

import com.cs251.backend.dto.request.BloodBagCreateRequest;
import com.cs251.backend.dto.request.BloodBagUpdateRequest;
import com.cs251.backend.entity.BloodBag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
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

    // ── Function 13: all bags ordered by BagID descending ────────────────────
    public List<BloodBag> findAll() {
        return jdbc.query(
                "SELECT BagID, ComponentType, BloodGroup, RhFactor, " +
                "CollectionDate, ExpiryDate, BagStatus, DonationID " +
                "FROM BloodBag ORDER BY BagID",
                BAG_MAPPER);
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

    // ── Blood bags for a specific donor (blood-testing page) ─────────────────
    public List<BloodBag> findByDonorId(Integer donorId) {
        return jdbc.query(
                "SELECT bb.BagID, bb.ComponentType, bb.BloodGroup, bb.RhFactor, " +
                "bb.CollectionDate, bb.ExpiryDate, bb.BagStatus, bb.DonationID " +
                "FROM BloodBag bb JOIN Donation d ON bb.DonationID = d.DonationID " +
                "WHERE d.DonorID = ? ORDER BY bb.CollectionDate DESC",
                BAG_MAPPER, donorId);
    }

    // ── Create new BloodBag after donation (add-blood-bag page) ─────────────
    public Integer create(BloodBagCreateRequest req) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO BloodBag (ComponentType, BloodGroup, RhFactor, CollectionDate, ExpiryDate, BagStatus, DonationID) " +
                    "VALUES (?, ?, ?, ?, ?, 1, ?)",  // 1 = Pending lab test
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, req.getComponentType());
            ps.setString(2, req.getBloodGroup());
            ps.setString(3, req.getRhFactor());
            ps.setDate(4, Date.valueOf(req.getCollectionDate()));
            ps.setDate(5, Date.valueOf(req.getExpiryDate()));
            ps.setInt(6, req.getDonationId());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
    }

    // ── Update bag status directly (discard / used) ──────────────────────────
    public void updateStatus(Integer bagId, int status) {
        jdbc.update("UPDATE BloodBag SET BagStatus = ? WHERE BagID = ?", status, bagId);
    }

    // ── Auto-expire: mark bags past ExpiryDate as BagStatus=4 ───────────────
    public void markExpiredBags() {
        jdbc.update(
            "UPDATE BloodBag SET BagStatus = 4 WHERE ExpiryDate < CURDATE() AND BagStatus IN (0, 1)");
    }

    // ── Available bags matching blood type (blood-usage page) ────────────────
    public List<BloodBag> findAvailableByBloodType(String bloodGroup, String rhFactor) {
        return jdbc.query(
                "SELECT BagID, ComponentType, BloodGroup, RhFactor, " +
                "CollectionDate, ExpiryDate, BagStatus, DonationID " +
                "FROM BloodBag WHERE BagStatus = 0 AND BloodGroup = ? AND RhFactor = ? " +
                "ORDER BY ExpiryDate ASC",
                BAG_MAPPER, bloodGroup, rhFactor);
    }
}
