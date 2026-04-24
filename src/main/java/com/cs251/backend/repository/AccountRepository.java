package com.cs251.backend.repository;

import com.cs251.backend.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AccountRepository — CALL sp_login
 * (suspend/reinstate ถูกย้ายไปอยู่ใน sp_suspend_donor / sp_reinstate_donor แล้ว)
 */
@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final JdbcTemplate jdbc;

    private static final RowMapper<Account> ACCOUNT_MAPPER = (rs, rn) -> Account.builder()
            .accountId(rs.getInt("AccountID"))
            .username(rs.getString("Username"))
            .password(rs.getString("Password"))
            .userType(rs.getString("UserType"))
            .status(rs.getInt("Status"))
            .referenceId(rs.getInt("ReferenceID"))
            .build();

    // ── Function 3: sp_login ─────────────────────────────────────────────────
    public Optional<Account> findByUsername(String username) {
        try {
            return Optional.ofNullable(
                jdbc.queryForObject("CALL sp_login(?)", ACCOUNT_MAPPER, username)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
