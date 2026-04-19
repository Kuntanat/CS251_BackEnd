package com.cs251.backend.repository;

import com.cs251.backend.entity.DonorContact;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Function 1, 6 */
@Repository
@RequiredArgsConstructor
public class DonorContactRepository {

    private final JdbcTemplate jdbc;

    // ── Function 1: INSERT contacts (batch) ───────────────────────────────────
    public void saveAll(List<DonorContact> contacts) {
        for (DonorContact c : contacts) {
            jdbc.update(
                "INSERT INTO DonorContact (ContactType, ContactValue, DonorID) VALUES (?, ?, ?)",
                c.getContactType(), c.getContactValue(), c.getDonorId()
            );
        }
    }

    // ── Function 6: UPDATE contact by type ───────────────────────────────────
    public void updateByDonorIdAndType(Integer donorId, String contactType, String newValue) {
        jdbc.update(
            "UPDATE DonorContact SET ContactValue=? WHERE DonorID=? AND ContactType=?",
            newValue, donorId, contactType
        );
    }
}
