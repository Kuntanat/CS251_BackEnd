-- Schema สำหรับ H2 in-memory (test profile)
-- ใช้ร่วมกับ application-test.yml

CREATE TABLE IF NOT EXISTS users (
    id         BIGINT        AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(100)  NOT NULL UNIQUE,
    email      VARCHAR(150)  NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,
    full_name  VARCHAR(200)  NULL,
    role       VARCHAR(20)   NOT NULL DEFAULT 'USER',
    enabled    BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP     NULL,
    updated_at TIMESTAMP     NULL
);
