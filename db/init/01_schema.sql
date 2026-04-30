-- ============================================================
--  CS251 Blood Bank Database - Schema
--  Group 9 | สร้าง: 2026
-- ============================================================

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS cs251_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cs251_db;

-- ────────────────────────────────────────────────────────────
--  1. Employee (ไม่มี FK → สร้างก่อน)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Employee (
    EmployeeID  INT          NOT NULL AUTO_INCREMENT,
    Name        VARCHAR(100) NOT NULL COMMENT 'ชื่อ-นามสกุลเจ้าหน้าที่',
    Role        VARCHAR(50)  NOT NULL COMMENT 'ตำแหน่ง เช่น Doctor, Nurse',
    Birthday    DATE         NOT NULL COMMENT 'วันเกิด',

    CONSTRAINT pk_employee PRIMARY KEY (EmployeeID)
) ENGINE=InnoDB COMMENT='ข้อมูลเจ้าหน้าที่';

-- ────────────────────────────────────────────────────────────
--  2. EmployeeContact
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS EmployeeContact (
    EmployeeContactID INT          NOT NULL AUTO_INCREMENT,
    ContactType       VARCHAR(255) NOT NULL COMMENT 'ประเภทการติดต่อ เช่น Email, Phone',
    ContactValue      VARCHAR(100) NOT NULL COMMENT 'ข้อมูลการติดต่อ',
    EmployeeID        INT          NOT NULL COMMENT 'FK → Employee',

    CONSTRAINT pk_employee_contact PRIMARY KEY (EmployeeContactID),
    CONSTRAINT fk_ec_employee      FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='ข้อมูลการติดต่อของเจ้าหน้าที่';

-- ────────────────────────────────────────────────────────────
--  3. Donor (ไม่มี FK → สร้างก่อน)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Donor (
    DonorID            INT          NOT NULL AUTO_INCREMENT,
    Name               VARCHAR(50)  NOT NULL COMMENT 'ชื่อผู้บริจาค',
    NationalID         CHAR(13)     NOT NULL COMMENT 'เลขประจำตัวประชาชน 13 หลัก',
    Gender             CHAR(1)      NOT NULL COMMENT 'M = ชาย, F = หญิง',
    Birthday           DATE         NOT NULL COMMENT 'วันเกิด',
    Status             TINYINT      NOT NULL DEFAULT 1 COMMENT '1=Active, 0=Inactive',
    Remark             VARCHAR(100)  NULL     COMMENT 'หมายเหตุ',
    BloodGroup         VARCHAR(2)   NOT NULL COMMENT 'หมู่เลือด A,B,AB,O',
    RhFactor           CHAR(1)      NOT NULL COMMENT 'Rh Factor + หรือ -',
    CongenitalDisease  VARCHAR(255) NULL     COMMENT 'โรคประจำตัว',

    CONSTRAINT pk_donor          PRIMARY KEY (DonorID),
    CONSTRAINT uq_donor_national UNIQUE      (NationalID),
    CONSTRAINT chk_donor_gender  CHECK (Gender IN ('M','F')),
    CONSTRAINT chk_donor_rh      CHECK (RhFactor IN ('+','-')),
    CONSTRAINT chk_donor_status  CHECK (Status IN (0,1))
) ENGINE=InnoDB COMMENT='ข้อมูลผู้บริจาคเลือด';

-- ────────────────────────────────────────────────────────────
--  4. DonorContact
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS DonorContact (
    DonorContactID INT          NOT NULL AUTO_INCREMENT,
    ContactType    VARCHAR(255) NOT NULL COMMENT 'ประเภทการติดต่อ เช่น Email, Phone',
    ContactValue   VARCHAR(100) NOT NULL COMMENT 'ข้อมูลการติดต่อ',
    DonorID        INT          NOT NULL COMMENT 'FK → Donor',

    CONSTRAINT pk_donor_contact PRIMARY KEY (DonorContactID),
    CONSTRAINT fk_dc_donor      FOREIGN KEY (DonorID) REFERENCES Donor(DonorID)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='ข้อมูลการติดต่อของผู้บริจาค';

-- ────────────────────────────────────────────────────────────
--  5. Patient
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Patient (
    PatientID         INT          NOT NULL AUTO_INCREMENT,
    NationalID        CHAR(13)     NOT NULL COMMENT 'เลขประจำตัวประชาชน',
    Name              VARCHAR(100) NOT NULL COMMENT 'ชื่อ-นามสกุลผู้ป่วย',
    Gender            CHAR(1)      NOT NULL COMMENT 'M หรือ F',
    BloodGroup        VARCHAR(2)   NOT NULL COMMENT 'หมู่เลือด A,B,AB,O',
    RhFactor          CHAR(1)      NOT NULL COMMENT 'Rh Factor + หรือ -',
    Birthday          DATE         NOT NULL COMMENT 'วันเกิด',
    TransfusionStatus VARCHAR(100)  NULL     COMMENT 'สถานะการรับเลือด',

    CONSTRAINT pk_patient          PRIMARY KEY (PatientID),
    CONSTRAINT uq_patient_national UNIQUE      (NationalID),
    CONSTRAINT chk_patient_gender  CHECK (Gender IN ('M','F')),
    CONSTRAINT chk_patient_rh      CHECK (RhFactor IN ('+','-'))
) ENGINE=InnoDB COMMENT='ข้อมูลผู้ป่วย';

-- ────────────────────────────────────────────────────────────
--  6. Account
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Account (
    AccountID   INT          NOT NULL AUTO_INCREMENT,
    Username    VARCHAR(50)  NOT NULL COMMENT 'ชื่อผู้ใช้ (unique)',
    Password    VARCHAR(255) NOT NULL COMMENT 'รหัสผ่านที่ผ่าน hash แล้ว',
    UserType    VARCHAR(20)  NOT NULL COMMENT 'Employee หรือ Donor',
    Status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1=ปกติ, 0=ระงับ',
    ReferenceID INT          NOT NULL COMMENT 'DonorID หรือ EmployeeID',

    CONSTRAINT pk_account          PRIMARY KEY (AccountID),
    CONSTRAINT uq_account_username UNIQUE      (Username),
    CONSTRAINT chk_account_type    CHECK (UserType IN ('Employee','Donor')),
    CONSTRAINT chk_account_status  CHECK (Status IN (0,1))
) ENGINE=InnoDB COMMENT='บัญชีผู้ใช้งานระบบ';

-- ────────────────────────────────────────────────────────────
--  7. Donation (FK → Donor, Employee)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Donation (
    DonationID       INT  NOT NULL AUTO_INCREMENT,
    DonationDate     DATE NOT NULL COMMENT 'วันที่บริจาค',
    NextEligibleDate DATE NOT NULL COMMENT 'วันที่บริจาคได้ครั้งถัดไป',
    Volume           INT  NOT NULL COMMENT 'ปริมาณเลือด (ml)',
    DonorID          INT  NOT NULL COMMENT 'FK → Donor',
    EmployeeID       INT  NOT NULL COMMENT 'FK → Employee (ผู้เจาะ)',

    CONSTRAINT pk_donation    PRIMARY KEY (DonationID),
    CONSTRAINT fk_don_donor   FOREIGN KEY (DonorID)    REFERENCES Donor(DonorID)       ON UPDATE CASCADE,
    CONSTRAINT fk_don_emp     FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)  ON UPDATE CASCADE
) ENGINE=InnoDB COMMENT='ข้อมูลการบริจาคเลือด';

-- ────────────────────────────────────────────────────────────
--  8. BloodBag (FK → Donation)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS BloodBag (
    BagID          INT          NOT NULL AUTO_INCREMENT,
    ComponentType  VARCHAR(20)  NOT NULL COMMENT 'PRC, FFP, Platelet ฯลฯ',
    BloodGroup     VARCHAR(2)   NOT NULL COMMENT 'หมู่เลือด A,B,AB,O',
    RhFactor       CHAR(1)      NOT NULL COMMENT 'Rh Factor + หรือ -',
    CollectionDate DATE         NOT NULL COMMENT 'วันที่รับเข้า',
    ExpiryDate     DATE         NOT NULL COMMENT 'วันหมดอายุ',
    BagStatus      TINYINT      NOT NULL DEFAULT 0
        COMMENT '0=Available, 1=Reserved, 2=Used, 3=Disposed',
    DonationID     INT          NOT NULL COMMENT 'FK → Donation',

    CONSTRAINT pk_blood_bag    PRIMARY KEY (BagID),
    CONSTRAINT fk_bb_donation  FOREIGN KEY (DonationID) REFERENCES Donation(DonationID) ON UPDATE CASCADE,
    CONSTRAINT chk_bb_rh       CHECK (RhFactor IN ('+','-')),
    CONSTRAINT chk_bb_status   CHECK (BagStatus IN (0,1,2,3,4))
) ENGINE=InnoDB COMMENT='ถุงเลือดและส่วนประกอบ';

-- ────────────────────────────────────────────────────────────
--  9. BloodTest (FK → BloodBag)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS BloodTest (
    TestID                   INT          NOT NULL AUTO_INCREMENT,
    InfectiousDiseaseResult  VARCHAR(100) NOT NULL COMMENT 'ผลตรวจเชื้อ',
    ConfirmatoryABO          VARCHAR(2)   NOT NULL COMMENT 'ยืนยันหมู่เลือด A,B,AB,O',
    ConfirmatoryRh           CHAR(1)      NOT NULL COMMENT 'ยืนยัน Rh + หรือ -',
    TestDate                 DATE         NOT NULL COMMENT 'วันที่ทดสอบ',
    BagID                    INT          NOT NULL COMMENT 'FK → BloodBag',

    CONSTRAINT pk_blood_test   PRIMARY KEY (TestID),
    CONSTRAINT fk_bt_bag       FOREIGN KEY (BagID) REFERENCES BloodBag(BagID) ON UPDATE CASCADE,
    CONSTRAINT chk_bt_rh       CHECK (ConfirmatoryRh IN ('+','-'))
) ENGINE=InnoDB COMMENT='ผลการตรวจเลือด';

-- ────────────────────────────────────────────────────────────
--  10. Deferral (FK → Donor, Employee)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Deferral (
    DeferralID     INT          NOT NULL AUTO_INCREMENT,
    DeferralType   VARCHAR(20)  NOT NULL COMMENT 'Temporary หรือ Permanent',
    ReasonCategory VARCHAR(100) NOT NULL COMMENT 'หมวดหมู่สาเหตุ',
    StartDate      DATE         NOT NULL COMMENT 'วันเริ่มระงับ',
    EndDate        DATE         NULL     COMMENT 'วันสิ้นสุด (NULL = Permanent)',
    DonorID        INT          NOT NULL COMMENT 'FK → Donor',
    EmployeeID     INT          NOT NULL COMMENT 'FK → Employee (ผู้บันทึก)',

    CONSTRAINT pk_deferral      PRIMARY KEY (DeferralID),
    CONSTRAINT fk_def_donor     FOREIGN KEY (DonorID)    REFERENCES Donor(DonorID)       ON UPDATE CASCADE,
    CONSTRAINT fk_def_emp       FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)  ON UPDATE CASCADE,
    CONSTRAINT chk_def_type     CHECK (DeferralType IN ('Temporary','Permanent'))
) ENGINE=InnoDB COMMENT='การระงับสิทธิ์บริจาค';

-- ────────────────────────────────────────────────────────────
--  11. BloodUsage (FK → Patient, BloodBag, Employee)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS BloodUsage (
    UsageID    INT  NOT NULL AUTO_INCREMENT,
    UsageDate  DATE NOT NULL COMMENT 'วันที่จ่ายเลือด',
    PatientID  INT  NOT NULL COMMENT 'FK → Patient',
    BagID      INT  NOT NULL COMMENT 'FK → BloodBag',
    EmployeeID INT  NOT NULL COMMENT 'FK → Employee (แพทย์ผู้สั่ง)',

    CONSTRAINT pk_blood_usage  PRIMARY KEY (UsageID),
    CONSTRAINT fk_bu_patient   FOREIGN KEY (PatientID)  REFERENCES Patient(PatientID)    ON UPDATE CASCADE,
    CONSTRAINT fk_bu_bag       FOREIGN KEY (BagID)      REFERENCES BloodBag(BagID)       ON UPDATE CASCADE,
    CONSTRAINT fk_bu_emp       FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)  ON UPDATE CASCADE
) ENGINE=InnoDB COMMENT='การจ่ายเลือดให้ผู้ป่วย';

-- ────────────────────────────────────────────────────────────
--  12. Disposal (FK → BloodBag, Employee)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Disposal (
    DisposalID   INT          NOT NULL AUTO_INCREMENT,
    DisposalDate DATE         NOT NULL COMMENT 'วันที่ทำลาย',
    Reason       VARCHAR(100) NOT NULL COMMENT 'เหตุผล เช่น หมดอายุ, ปนเปื้อน',
    BagID        INT          NOT NULL COMMENT 'FK → BloodBag',
    EmployeeID   INT          NOT NULL COMMENT 'FK → Employee (ผู้ทำรายการ)',

    CONSTRAINT pk_disposal   PRIMARY KEY (DisposalID),
    CONSTRAINT fk_dis_bag    FOREIGN KEY (BagID)      REFERENCES BloodBag(BagID)       ON UPDATE CASCADE,
    CONSTRAINT fk_dis_emp    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)  ON UPDATE CASCADE
) ENGINE=InnoDB COMMENT='การทำลายถุงเลือด';

-- ────────────────────────────────────────────────────────────
--  Indexes เพิ่มเติมเพื่อ performance
-- ────────────────────────────────────────────────────────────
CREATE INDEX idx_donor_blood     ON Donor(BloodGroup, RhFactor);
CREATE INDEX idx_bag_status      ON BloodBag(BagStatus, BloodGroup, RhFactor);
CREATE INDEX idx_bag_expiry      ON BloodBag(ExpiryDate);
CREATE INDEX idx_donation_donor  ON Donation(DonorID);
CREATE INDEX idx_deferral_donor  ON Deferral(DonorID);
CREATE INDEX idx_usage_patient   ON BloodUsage(PatientID);
CREATE INDEX idx_account_ref     ON Account(UserType, ReferenceID);

-- ────────────────────────────────────────────────────────────
--  Function 19: Trigger — อัปเดต BagStatus อัตโนมัติหลังจ่ายเลือด
-- ────────────────────────────────────────────────────────────
DELIMITER //
CREATE TRIGGER after_blood_usage_insert
AFTER INSERT ON BloodUsage
FOR EACH ROW
BEGIN
    UPDATE BloodBag
    SET BagStatus = 2
    WHERE BagID = NEW.BagID;
END //
DELIMITER ;

