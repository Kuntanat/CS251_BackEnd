package com.cs251.backend.repository;

import com.cs251.backend.entity.EmployeeContact;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Function 2 */
@Repository
@RequiredArgsConstructor
public class EmployeeContactRepository {

    private final JdbcTemplate jdbc;

    // ── Function 2: INSERT contacts (batch) ───────────────────────────────────
    public void saveAll(List<EmployeeContact> contacts) {
        for (EmployeeContact c : contacts) {
            jdbc.update(
                "INSERT INTO EmployeeContact (ContactType, ContactValue, EmployeeID) VALUES (?, ?, ?)",
                c.getContactType(), c.getContactValue(), c.getEmployeeId()
            );
        }
    }
}
