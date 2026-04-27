package com.cs251.backend.repository;

import com.cs251.backend.dto.response.DonorDonationHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * DonationReadRepository — ใช้สำหรับ query ฝั่งแสดงผล (History)
 * ไม่ผูกกับ stored procedure เดิม เพื่อให้รองรับหน้าจอ Figma ได้ครบ
 */
@Repository
@RequiredArgsConstructor
public class DonationReadRepository {

    private final JdbcTemplate jdbc;

    private static final RowMapper<DonorDonationHistoryResponse> HISTORY_MAPPER = (rs, rn) ->
            DonorDonationHistoryResponse.builder()
                    .donationDate(rs.getDate("donationDate") != null ? rs.getDate("donationDate").toLocalDate() : null)
                    .donationType(rs.getString("donationType"))
                    .volume(rs.getObject("volume") != null ? rs.getInt("volume") : null)
                    .screeningResult(rs.getString("screeningResult"))
                    .build();

    public List<DonorDonationHistoryResponse> findHistoryByDonorId(Integer donorId) {
        return jdbc.query(
                """
                SELECT
                    dn.DonationDate AS donationDate,
                    COALESCE(bb.ComponentType, 'Whole Blood') AS donationType,
                    dn.Volume AS volume,
                    CASE WHEN dn.Volume > 0 THEN 'ผ่าน' ELSE 'ไม่ผ่าน' END AS screeningResult
                FROM Donation dn
                LEFT JOIN BloodBag bb ON bb.DonationID = dn.DonationID
                WHERE dn.DonorID = ?
                ORDER BY dn.DonationDate DESC, dn.DonationID DESC
                """,
                HISTORY_MAPPER,
                donorId
        );
    }
}

