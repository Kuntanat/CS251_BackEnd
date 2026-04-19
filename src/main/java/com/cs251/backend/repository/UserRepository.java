package com.cs251.backend.repository;

import com.cs251.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * UserRepository — ใช้ JdbcTemplate (raw SQL) แทน JPA
 */
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbc;

    /** RowMapper: แปลง ResultSet → User POJO */
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .fullName(rs.getString("full_name"))
                .role(User.Role.valueOf(rs.getString("role")))
                .enabled(rs.getBoolean("enabled"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .updatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null)
                .build();
    };

    // ─────────────────────────────────────────
    //  Queries
    // ─────────────────────────────────────────

    public Optional<User> findByUsername(String username) {
        try {
            User user = jdbc.queryForObject(
                    "SELECT * FROM users WHERE username = ?",
                    USER_ROW_MAPPER,
                    username
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = jdbc.queryForObject(
                    "SELECT * FROM users WHERE email = ?",
                    USER_ROW_MAPPER,
                    email
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?",
                Integer.class,
                username
        );
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE email = ?",
                Integer.class,
                email
        );
        return count != null && count > 0;
    }

    // ─────────────────────────────────────────
    //  Mutations
    // ─────────────────────────────────────────

    /**
     * INSERT หรือ UPDATE ขึ้นอยู่กับว่า id มีค่าหรือเปล่า
     */
    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        }
        return update(user);
    }

    private User insert(User user) {
        final String sql =
                "INSERT INTO users (username, email, password, full_name, role, enabled, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getRole().name());
            ps.setBoolean(6, user.isEnabled());
            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            user.setId(generatedId.longValue());
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    private User update(User user) {
        jdbc.update(
                "UPDATE users SET email=?, password=?, full_name=?, role=?, enabled=?, updated_at=NOW() WHERE id=?",
                user.getEmail(),
                user.getPassword(),
                user.getFullName(),
                user.getRole().name(),
                user.isEnabled(),
                user.getId()
        );
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
