# CS251 Blood Bank Backend API

> Backend API สำหรับระบบจัดการธนาคารเลือด — CS251 Group Project กลุ่มที่ 9

---

## 📋 สารบัญ

- [ภาพรวมโปรเจกต์](#ภาพรวมโปรเจกต์)
- [Tech Stack](#tech-stack)
- [โครงสร้างโปรเจกต์](#โครงสร้างโปรเจกต์)
- [การติดตั้งและรัน](#การติดตั้งและรัน)
- [Environment Variables](#environment-variables)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)

---

## ภาพรวมโปรเจกต์

ระบบ Backend สำหรับจัดการข้อมูลธนาคารเลือด ประกอบด้วย:

- **การจัดการผู้บริจาค** (Donor Management)
- **การจัดการถุงเลือด** (Blood Bag Management)
- **การตรวจเลือด** (Blood Testing)
- **การจ่ายเลือดให้ผู้ป่วย** (Blood Usage / Transfusion)
- **การระงับสิทธิ์บริจาค** (Deferral Management)
- **ระบบ Authentication** ด้วย JWT

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2.5 |
| Security | Spring Security + JWT (JJWT 0.12.5) |
| Database | MySQL 8.0 |
| Data Access | Spring JDBC (`JdbcTemplate`) — raw SQL, ไม่ใช้ ORM |
| Build Tool | Maven |
| Container | Docker + Docker Compose |
| API Docs | Swagger / OpenAPI 3 (springdoc) |
| Config | spring-dotenv (.env support) |

---

## โครงสร้างโปรเจกต์

```
CS251_BackEnd/
├── docker-compose.yml              # Docker services (MySQL, App, phpMyAdmin)
├── Dockerfile                      # Multi-stage build image
├── pom.xml                         # Maven dependencies
├── .env                            # ⚠️ ไม่ commit — ค่าจริง
├── .env.example                    # ✅ template สำหรับทีม
│
├── db/
│   └── init/
│       ├── 01_schema.sql           # สร้าง tables ทั้งหมด
│       └── 02_sample_data.sql      # ข้อมูลทดสอบ
│
└── src/
    ├── main/
    │   ├── java/com/cs251/backend/
    │   │   ├── CS251BackEndApplication.java
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java     # Security rules, CORS, JWT filter
    │   │   │   ├── OpenApiConfig.java      # Swagger UI config
    │   │   │   └── JpaConfig.java          # JPA Auditing
    │   │   ├── controller/
    │   │   │   └── AuthController.java     # /api/auth/*
    │   │   ├── dto/
    │   │   │   ├── request/               # Request body DTOs
    │   │   │   └── response/              # Response DTOs
    │   │   ├── entity/
    │   │   │   └── User.java
    │   │   ├── exception/
    │   │   │   └── GlobalExceptionHandler.java
    │   │   ├── repository/
    │   │   │   └── UserRepository.java
    │   │   ├── security/
    │   │   │   ├── JwtTokenProvider.java
    │   │   │   ├── JwtAuthenticationFilter.java
    │   │   │   └── UserDetailsServiceImpl.java
    │   │   └── service/
    │   │       └── AuthService.java
    │   └── resources/
    │       └── application.yml             # Config อ่านจาก .env
    └── test/
        ├── java/.../
        │   └── CS251BackEndApplicationTests.java
        └── resources/
            └── application-test.yml        # H2 in-memory สำหรับ test
```

---

## การติดตั้งและรัน

### ข้อกำหนดเบื้องต้น

| เครื่องมือ | Version |
|---|---|
| Java | 21+ |
| Maven | 3.9+ |
| Docker | 24+ |
| Docker Compose | 2.x |

---

### วิธีที่ 1: รันด้วย Docker Compose (แนะนำ)

```bash
# 1. Clone repository
git clone https://github.com/your-org/CS251_BackEnd.git
cd CS251_BackEnd

# 2. ตั้งค่า environment
cp .env.example .env
# แก้ไข .env ตามข้อมูลจริง

# 3. รัน MySQL + App
docker compose up -d

# 4. รัน MySQL + App + phpMyAdmin (dev mode)
docker compose --profile dev up -d

# ดู logs
docker compose logs -f app
```

> **MySQL จะรัน SQL scripts อัตโนมัติ** (`db/init/`) เมื่อสร้าง container ครั้งแรก

---

### วิธีที่ 2: รันตรง (Local)

```bash
# 1. สร้าง MySQL database
mysql -u root -p
CREATE DATABASE cs251_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit;

# 2. รัน schema + sample data
mysql -u root -p cs251_db < db/init/01_schema.sql
mysql -u root -p cs251_db < db/init/02_sample_data.sql

# 3. ตั้งค่า .env
cp .env.example .env
# แก้ไข DB_PASSWORD และ JWT_SECRET

# 4. Build และรัน
mvn spring-boot:run
```

---

### Reset Database

```bash
# ลบ volume แล้วสร้างใหม่ (ข้อมูลจะหายทั้งหมด)
docker compose down -v
docker compose up -d
```

---

## Environment Variables

คัดลอก `.env.example` → `.env` แล้วแก้ไขค่าต่อไปนี้:

| Variable | Default | จำเป็น | คำอธิบาย |
|---|---|---|---|
| `SERVER_PORT` | `8080` | | Port ของ application |
| `DB_HOST` | `localhost` | | Host ของ MySQL |
| `DB_PORT` | `3306` | | Port ของ MySQL |
| `DB_NAME` | `cs251_db` | | ชื่อ Database |
| `DB_USERNAME` | `root` | | MySQL username |
| `DB_PASSWORD` | — | ✅ | MySQL password |
| `DB_POOL_SIZE` | `10` | | จำนวน connection pool |
| `JPA_DDL_AUTO` | `update` | | `update` / `validate` / `none` |
| `JPA_SHOW_SQL` | `false` | | แสดง SQL ใน log |
| `JWT_SECRET` | — | ✅ | Secret key (อย่างน้อย 32 ตัวอักษร) |
| `JWT_EXPIRATION` | `86400000` | | อายุ token (ms) — default 24 ชม. |
| `JWT_REFRESH_EXPIRATION` | `604800000` | | อายุ refresh token — default 7 วัน |
| `LOG_LEVEL` | `INFO` | | Log level ของ application |

> ⚠️ **ห้าม commit ไฟล์ `.env`** — มีใน `.gitignore` แล้ว

---

## Database Schema

### ER Diagram (ความสัมพันธ์)

```
Employee ─────────────────┬──< EmployeeContact
    │                     │
    ├──< Donation >── Donor ──< DonorContact
    │        │
    │        └──> BloodBag ──< BloodTest
    │                 │
    ├──< BloodUsage >─┤
    │                 │
    ├──< Disposal >───┘
    │
    └──< Deferral >── Donor

Donor / Employee ──< Account

BloodUsage >── Patient
```

---

### รายละเอียดตาราง

#### Donor — ผู้บริจาคเลือด

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| DonorID | INT | PK, AUTO_INCREMENT | รหัสผู้บริจาค |
| Name | VARCHAR(50) | NOT NULL | ชื่อผู้บริจาค |
| NationalID | CHAR(13) | UNIQUE, NOT NULL | เลขประจำตัวประชาชน |
| Gender | CHAR(1) | CHECK (M/F) | เพศ |
| Birthday | DATE | NOT NULL | วันเกิด |
| Status | TINYINT | CHECK (0/1) | 1=Active, 0=Inactive |
| Remark | VARCHAR(20) | NULL | หมายเหตุ |
| BloodGroup | VARCHAR(2) | NOT NULL | หมู่เลือด (A/B/AB/O) |
| RhFactor | CHAR(1) | CHECK (+/-) | Rh Factor |
| CongenitalDisease | VARCHAR(255) | NULL | โรคประจำตัว |

#### DonorContact — ข้อมูลติดต่อผู้บริจาค

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| DonorContactID | INT | PK, AUTO_INCREMENT | รหัส |
| ContactType | VARCHAR(255) | NOT NULL | ประเภท (Email, Phone) |
| ContactValue | VARCHAR(100) | NOT NULL | ข้อมูลการติดต่อ |
| DonorID | INT | FK → Donor | รหัสผู้บริจาค |

#### Employee — เจ้าหน้าที่

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| EmployeeID | INT | PK, AUTO_INCREMENT | รหัสเจ้าหน้าที่ |
| Name | VARCHAR(100) | NOT NULL | ชื่อ-นามสกุล |
| Role | VARCHAR(50) | NOT NULL | ตำแหน่ง (Doctor, Nurse ฯลฯ) |
| Birthday | DATE | NOT NULL | วันเกิด |

#### EmployeeContact — ข้อมูลติดต่อเจ้าหน้าที่

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| EmployeeContactID | INT | PK, AUTO_INCREMENT | รหัส |
| ContactType | VARCHAR(255) | NOT NULL | ประเภท (Email, Phone) |
| ContactValue | VARCHAR(100) | NOT NULL | ข้อมูลการติดต่อ |
| EmployeeID | INT | FK → Employee | รหัสเจ้าหน้าที่ |

#### Donation — การบริจาคเลือด

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| DonationID | INT | PK, AUTO_INCREMENT | รหัสการบริจาค |
| DonationDate | DATE | NOT NULL | วันที่บริจาค |
| NextEligibleDate | DATE | NOT NULL | วันที่บริจาคได้ครั้งถัดไป |
| Volume | INT | NOT NULL | ปริมาณเลือด (ml) |
| DonorID | INT | FK → Donor | รหัสผู้บริจาค |
| EmployeeID | INT | FK → Employee | รหัสเจ้าหน้าที่เจาะเลือด |

#### BloodBag — ถุงเลือด

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| BagID | INT | PK, AUTO_INCREMENT | รหัสถุงเลือด |
| ComponentType | VARCHAR(20) | NOT NULL | PRC / FFP / Platelet |
| BloodGroup | VARCHAR(2) | NOT NULL | หมู่เลือด (A/B/AB/O) |
| RhFactor | CHAR(1) | CHECK (+/-) | Rh Factor |
| CollectionDate | DATE | NOT NULL | วันที่รับเข้า |
| ExpiryDate | DATE | NOT NULL | วันหมดอายุ |
| BagStatus | TINYINT | CHECK (0-3) | 0=Available, 1=Reserved, 2=Used, 3=Disposed |
| DonationID | INT | FK → Donation | รหัสการบริจาค |

#### BloodTest — ผลการตรวจเลือด

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| TestID | INT | PK, AUTO_INCREMENT | รหัสการทดสอบ |
| InfectiousDiseaseResult | VARCHAR(100) | NOT NULL | ผลตรวจเชื้อ |
| ConfirmatoryABO | VARCHAR(2) | NOT NULL | ยืนยันหมู่เลือด |
| ConfirmatoryRh | CHAR(1) | CHECK (+/-) | ยืนยัน Rh Factor |
| TestDate | DATE | NOT NULL | วันที่ทดสอบ |
| BagID | INT | FK → BloodBag | รหัสถุงเลือด |

#### Deferral — การระงับสิทธิ์บริจาค

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| DeferralID | INT | PK, AUTO_INCREMENT | รหัส |
| DeferralType | VARCHAR(20) | CHECK (Temporary/Permanent) | ประเภทการระงับ |
| ReasonCategory | VARCHAR(100) | NOT NULL | หมวดหมู่สาเหตุ |
| StartDate | DATE | NOT NULL | วันเริ่มระงับ |
| EndDate | DATE | NULL | วันสิ้นสุด (NULL = Permanent) |
| DonorID | INT | FK → Donor | รหัสผู้บริจาค |
| EmployeeID | INT | FK → Employee | รหัสผู้บันทึก |

#### Patient — ผู้ป่วย

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| PatientID | INT | PK, AUTO_INCREMENT | รหัสผู้ป่วย |
| NationalID | CHAR(13) | UNIQUE, NOT NULL | เลขประจำตัวประชาชน |
| Name | VARCHAR(100) | NOT NULL | ชื่อ-นามสกุล |
| Gender | CHAR(1) | CHECK (M/F) | เพศ |
| BloodGroup | VARCHAR(2) | NOT NULL | หมู่เลือด |
| RhFactor | CHAR(1) | CHECK (+/-) | Rh Factor |
| Birthday | DATE | NOT NULL | วันเกิด |
| TransfusionStatus | VARCHAR(20) | NULL | สถานะการรับเลือด |

#### BloodUsage — การจ่ายเลือด

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| UsageID | INT | PK, AUTO_INCREMENT | รหัสการจ่าย |
| UsageDate | DATE | NOT NULL | วันที่จ่ายเลือด |
| PatientID | INT | FK → Patient | รหัสผู้ป่วย |
| BagID | INT | FK → BloodBag | รหัสถุงเลือด |
| EmployeeID | INT | FK → Employee | รหัสแพทย์ผู้สั่ง |

#### Disposal — การทำลายถุงเลือด

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| DisposalID | INT | PK, AUTO_INCREMENT | รหัส |
| DisposalDate | DATE | NOT NULL | วันที่ทำลาย |
| Reason | VARCHAR(100) | NOT NULL | เหตุผล |
| BagID | INT | FK → BloodBag | รหัสถุงเลือด |
| EmployeeID | INT | FK → Employee | รหัสผู้ทำรายการ |

#### Account — บัญชีผู้ใช้

| Column | Type | Constraint | คำอธิบาย |
|---|---|---|---|
| AccountID | INT | PK, AUTO_INCREMENT | รหัสบัญชี |
| Username | VARCHAR(50) | UNIQUE, NOT NULL | ชื่อผู้ใช้ |
| Password | VARCHAR(255) | NOT NULL | รหัสผ่านที่ผ่าน BCrypt hash |
| UserType | VARCHAR(20) | CHECK (Employee/Donor) | ประเภทผู้ใช้ |
| Status | TINYINT | CHECK (0/1) | 1=ปกติ, 0=ระงับ |
| ReferenceID | INT | NOT NULL | DonorID หรือ EmployeeID |

---

## API Endpoints

### Base URL

```
http://localhost:8080
```

### Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

### Authentication

#### POST `/api/auth/register` — ลงทะเบียน

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "secret123",
  "fullName": "John Doe"
}
```

**Response `201 Created`:**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": {
      "id": 1,
      "username": "john_doe",
      "email": "john@example.com",
      "fullName": "John Doe",
      "role": "USER"
    }
  }
}
```

---

#### POST `/api/auth/login` — เข้าสู่ระบบ

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "secret123"
}
```

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": { ... }
  }
}
```

---

#### GET `/api/auth/me` — ข้อมูลตัวเอง 🔒

**Headers:**
```
Authorization: Bearer <token>
```

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "USER"
  }
}
```

---

### Error Responses

| HTTP Status | เกิดเมื่อ |
|---|---|
| `400 Bad Request` | ข้อมูล input ไม่ถูกต้อง |
| `401 Unauthorized` | Token ไม่ถูกต้องหรือหมดอายุ |
| `403 Forbidden` | ไม่มีสิทธิ์เข้าถึง |
| `500 Internal Server Error` | ข้อผิดพลาดของ server |

**ตัวอย่าง Error Response:**
```json
{
  "success": false,
  "message": "Invalid username or password"
}
```

**ตัวอย่าง Validation Error:**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": "Email must be valid",
    "password": "Password must be at least 6 characters"
  }
}
```

---

## Authentication

ระบบใช้ **JWT (JSON Web Token)** แบบ Stateless

### การใช้งาน Token

1. เรียก `/api/auth/login` เพื่อรับ `accessToken`
2. ใส่ token ใน Header ทุก request ที่ต้องการ authentication:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Roles

| Role | สิทธิ์ |
|---|---|
| `USER` | เข้าถึง endpoint ทั่วไป |
| `ADMIN` | เข้าถึงทุก endpoint รวมถึงระบบจัดการ |

---

## Docker Commands

```bash
# รัน services ทั้งหมด
docker compose up -d

# รันพร้อม phpMyAdmin
docker compose --profile dev up -d

# หยุด services
docker compose down

# หยุดและลบ volume (reset database)
docker compose down -v

# ดู logs
docker compose logs -f
docker compose logs -f app
docker compose logs -f mysql

# เข้า MySQL shell
docker compose exec mysql mysql -u root -p cs251_db
```

---

## สมาชิกกลุ่ม

CS251 Group Project — กลุ่มที่ 9
