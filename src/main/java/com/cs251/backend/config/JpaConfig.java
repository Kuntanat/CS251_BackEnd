package com.cs251.backend.config;

import org.springframework.context.annotation.Configuration;

/**
 * ไม่ใช้ JPA/Hibernate — ถูกแทนที่ด้วย JdbcTemplate (raw SQL)
 * ไฟล์นี้เก็บไว้เผื่อต้องการ configuration เพิ่มเติมในอนาคต
 */
@Configuration
public class JpaConfig {
    // intentionally empty — no JPA auditing needed
}
