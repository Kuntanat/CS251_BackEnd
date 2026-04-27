package com.cs251.backend.repository;

import com.cs251.backend.dto.response.AlertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AlertRepository {

    private final JdbcTemplate jdbc;

    public List<AlertResponse> getAlerts() {
        List<AlertResponse> out = new ArrayList<>();

        // 1) Expiring soon (<= 3 days)
        List<Map<String, Object>> expiring = jdbc.queryForList(
                """
                SELECT BagID, ComponentType, BloodGroup, RhFactor, ExpiryDate
                FROM BloodBag
                WHERE BagStatus = 0
                  AND ExpiryDate <= DATE_ADD(CURDATE(), INTERVAL 3 DAY)
                ORDER BY ExpiryDate ASC
                LIMIT 10
                """
        );

        for (Map<String, Object> r : expiring) {
            LocalDate exp = r.get("ExpiryDate") instanceof java.sql.Date d ? d.toLocalDate() : null;
            out.add(AlertResponse.builder()
                    .type("EXPIRING_SOON")
                    .severity("MEDIUM")
                    .title("ถุงเลือดใกล้หมดอายุภายใน 3 วัน")
                    .message(String.format("%s ถุงรหัส BAG-%s จะหมดอายุวันที่ %s",
                            r.get("ComponentType"),
                            r.get("BagID"),
                            exp))
                    .date(exp)
                    .meta(Map.of(
                            "bagId", r.get("BagID"),
                            "componentType", r.get("ComponentType"),
                            "bloodGroup", r.get("BloodGroup"),
                            "rhFactor", r.get("RhFactor")
                    ))
                    .build());
        }

        // 2) Low stock
        int lowThreshold = 5;
        List<Map<String, Object>> lowStock = jdbc.queryForList(
                """
                SELECT BloodGroup, RhFactor, ComponentType, COUNT(*) AS Units
                FROM BloodBag
                WHERE BagStatus = 0
                GROUP BY BloodGroup, RhFactor, ComponentType
                HAVING COUNT(*) <= ?
                ORDER BY Units ASC
                LIMIT 10
                """,
                lowThreshold
        );

        for (Map<String, Object> r : lowStock) {
            out.add(AlertResponse.builder()
                    .type("LOW_STOCK")
                    .severity("HIGH")
                    .title("สต็อกเลือดต่ำกว่ามาตรฐาน")
                    .message(String.format("เลือด %s%s (%s) คงเหลือ %s ยูนิต",
                            r.get("BloodGroup"),
                            r.get("RhFactor"),
                            r.get("ComponentType"),
                            r.get("Units")))
                    .date(LocalDate.now())
                    .meta(new HashMap<>(r))
                    .build());
        }

        return out;
    }
}

