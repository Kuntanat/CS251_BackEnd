-- ============================================================
--  CS251 Blood Bank — Stored Procedures (24 Functions)
--  DAO ฝั่ง Java จะ CALL procedure เหล่านี้แทนการเขียน SQL ตรงๆ
-- ============================================================

USE cs251_db;

-- ════════════════════════════════════════════════════════════
--  Function 1: ลงทะเบียน Donor
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_register_donor;
DELIMITER //
CREATE PROCEDURE sp_register_donor(
    IN  p_name              VARCHAR(50),
    IN  p_nationalId        CHAR(13),
    IN  p_gender            CHAR(1),
    IN  p_birthday          DATE,
    IN  p_bloodGroup        VARCHAR(2),
    IN  p_rhFactor          CHAR(1),
    IN  p_congenitalDisease VARCHAR(255),
    IN  p_email             VARCHAR(100),
    IN  p_phone             VARCHAR(100),
    IN  p_place             VARCHAR(100),
    IN  p_username          VARCHAR(50),
    IN  p_hashedPassword    VARCHAR(255),
    OUT p_donorId           INT
)
BEGIN
    START TRANSACTION;

    INSERT INTO Donor (Name, NationalID, Gender, Birthday, BloodGroup, RhFactor, CongenitalDisease)
    VALUES (p_name, p_nationalId, p_gender, p_birthday, p_bloodGroup, p_rhFactor, p_congenitalDisease);

    SET p_donorId = LAST_INSERT_ID();

    INSERT INTO DonorContact (ContactType, ContactValue, DonorID)
    VALUES ('Email', p_email, p_donorId),
           ('Phone', p_phone, p_donorId),
           ('Place', p_place, p_donorId);

    INSERT INTO Account (Username, Password, UserType, ReferenceID)
    VALUES (p_username, p_hashedPassword, 'Donor', p_donorId);

    COMMIT;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 2: ลงทะเบียน Employee
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_register_employee;
DELIMITER //
CREATE PROCEDURE sp_register_employee(
    IN  p_name           VARCHAR(100),
    IN  p_role           VARCHAR(50),
    IN  p_birthday       DATE,
    IN  p_email          VARCHAR(100),
    IN  p_phone          VARCHAR(100),
    IN  p_username       VARCHAR(50),
    IN  p_hashedPassword VARCHAR(255),
    OUT p_employeeId     INT
)
BEGIN
    START TRANSACTION;

    INSERT INTO Employee (Name, Role, Birthday) VALUES (p_name, p_role, p_birthday);
    SET p_employeeId = LAST_INSERT_ID();

    INSERT INTO EmployeeContact (ContactType, ContactValue, EmployeeID)
    VALUES ('Email', p_email, p_employeeId),
           ('Phone', p_phone, p_employeeId);

    INSERT INTO Account (Username, Password, UserType, ReferenceID)
    VALUES (p_username, p_hashedPassword, 'Employee', p_employeeId);

    COMMIT;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 3: เข้าสู่ระบบ
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_login;
DELIMITER //
CREATE PROCEDURE sp_login(IN p_username VARCHAR(50))
BEGIN
    SELECT AccountID, ReferenceID, UserType, Password, Username, Status
    FROM   Account
    WHERE  Username = p_username
    LIMIT  1;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 4: แสดงตาราง Donor
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_list_donors;
DELIMITER //
CREATE PROCEDURE sp_list_donors()
BEGIN
    SELECT d.DonorID, d.Name, d.NationalID, d.Gender, d.Birthday,
           d.Status, d.Remark, d.BloodGroup, d.RhFactor, d.CongenitalDisease,
           MAX(dn.DonationDate) AS LatestDonation
    FROM   Donor d
    LEFT JOIN Donation dn ON d.DonorID = dn.DonorID
    GROUP  BY d.DonorID, d.Name, d.NationalID, d.Gender, d.Birthday,
              d.Status, d.Remark, d.BloodGroup, d.RhFactor, d.CongenitalDisease;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 5: แสดงตาราง Patient
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_list_patients;
DELIMITER //
CREATE PROCEDURE sp_list_patients()
BEGIN
    SELECT PatientID, NationalID, Name, Gender, BloodGroup, RhFactor,
           Birthday, TransfusionStatus
    FROM   Patient;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 6: แก้ไขข้อมูล Donor
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_update_donor;
DELIMITER //
CREATE PROCEDURE sp_update_donor(
    IN p_donorId           INT,
    IN p_name              VARCHAR(50),
    IN p_birthday          DATE,
    IN p_congenitalDisease VARCHAR(255),
    IN p_phone             VARCHAR(100),
    IN p_email             VARCHAR(100),
    IN p_place             VARCHAR(100)
)
BEGIN
    START TRANSACTION;

    UPDATE Donor
    SET    Name = p_name, Birthday = p_birthday, CongenitalDisease = p_congenitalDisease
    WHERE  DonorID = p_donorId;

    UPDATE DonorContact SET ContactValue = p_phone WHERE DonorID = p_donorId AND ContactType = 'Phone';
    UPDATE DonorContact SET ContactValue = p_email WHERE DonorID = p_donorId AND ContactType = 'Email';
    UPDATE DonorContact SET ContactValue = p_place WHERE DonorID = p_donorId AND ContactType = 'Place';

    COMMIT;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 7: ระงับสิทธิ์ผู้บริจาค
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_suspend_donor;
DELIMITER //
CREATE PROCEDURE sp_suspend_donor(IN p_donorId INT, IN p_remark VARCHAR(20))
BEGIN
    START TRANSACTION;
    UPDATE Donor   SET Status = 0, Remark = p_remark WHERE DonorID = p_donorId;
    UPDATE Account SET Status = 0 WHERE ReferenceID = p_donorId AND UserType = 'Donor';
    COMMIT;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 8: ยกเลิกการระงับสิทธิ์
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_reinstate_donor;
DELIMITER //
CREATE PROCEDURE sp_reinstate_donor(IN p_donorId INT, IN p_remark VARCHAR(20))
BEGIN
    START TRANSACTION;
    UPDATE Donor   SET Status = 1, Remark = p_remark WHERE DonorID = p_donorId;
    UPDATE Account SET Status = 1 WHERE ReferenceID = p_donorId AND UserType = 'Donor';
    COMMIT;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 9: เพิ่มผู้ป่วยใหม่
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_add_patient;
DELIMITER //
CREATE PROCEDURE sp_add_patient(
    IN  p_nationalId        CHAR(13),
    IN  p_name              VARCHAR(100),
    IN  p_gender            CHAR(1),
    IN  p_bloodGroup        VARCHAR(2),
    IN  p_rhFactor          CHAR(1),
    IN  p_birthday          DATE,
    IN  p_transfusionStatus VARCHAR(20),
    OUT p_patientId         INT
)
BEGIN
    INSERT INTO Patient (NationalID, Name, Gender, BloodGroup, RhFactor, Birthday, TransfusionStatus)
    VALUES (p_nationalId, p_name, p_gender, p_bloodGroup, p_rhFactor, p_birthday, p_transfusionStatus);
    SET p_patientId = LAST_INSERT_ID();
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 10: แก้ไขข้อมูลผู้ป่วย
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_update_patient;
DELIMITER //
CREATE PROCEDURE sp_update_patient(
    IN p_patientId         INT,
    IN p_nationalId        CHAR(13),
    IN p_name              VARCHAR(100),
    IN p_gender            CHAR(1),
    IN p_bloodGroup        VARCHAR(2),
    IN p_rhFactor          CHAR(1),
    IN p_birthday          DATE,
    IN p_transfusionStatus VARCHAR(20)
)
BEGIN
    UPDATE Patient
    SET    NationalID = p_nationalId, Name = p_name, Gender = p_gender,
           BloodGroup = p_bloodGroup, RhFactor = p_rhFactor,
           Birthday = p_birthday, TransfusionStatus = p_transfusionStatus
    WHERE  PatientID = p_patientId;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 11: ค้นหาผู้บริจาค
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_search_donor;
DELIMITER //
CREATE PROCEDURE sp_search_donor(IN p_name VARCHAR(50), IN p_donorId INT)
BEGIN
    SELECT d.DonorID, d.Name, d.NationalID, d.Gender, d.Birthday,
           d.Status, d.Remark, d.BloodGroup, d.RhFactor, d.CongenitalDisease,
           MAX(dn.DonationDate) AS LatestDonation
    FROM   Donor d
    LEFT JOIN Donation dn ON d.DonorID = dn.DonorID
    WHERE  d.Name LIKE CONCAT('%', p_name, '%') OR d.DonorID = p_donorId
    GROUP  BY d.DonorID, d.Name, d.NationalID, d.Gender, d.Birthday,
              d.Status, d.Remark, d.BloodGroup, d.RhFactor, d.CongenitalDisease;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 12: ค้นหาผู้ป่วย
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_search_patient;
DELIMITER //
CREATE PROCEDURE sp_search_patient(IN p_name VARCHAR(100), IN p_patientId INT)
BEGIN
    SELECT PatientID, NationalID, Name, Gender, BloodGroup, RhFactor,
           Birthday, TransfusionStatus
    FROM   Patient
    WHERE  Name LIKE CONCAT('%', p_name, '%') OR PatientID = p_patientId;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 13: แสดงตารางคลังเลือด
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_list_blood_inventory;
DELIMITER //
CREATE PROCEDURE sp_list_blood_inventory()
BEGIN
    SELECT BagID, ComponentType, BloodGroup, RhFactor,
           CollectionDate, ExpiryDate, BagStatus, DonationID
    FROM   BloodBag
    ORDER  BY ExpiryDate ASC;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 14: หาเลือดใกล้หมดอายุ (≤ 7 วัน, Available)
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_find_expiring_blood;
DELIMITER //
CREATE PROCEDURE sp_find_expiring_blood()
BEGIN
    SELECT BagID, BloodGroup, RhFactor, ComponentType,
           CollectionDate, ExpiryDate, BagStatus, DonationID,
           DATEDIFF(ExpiryDate, CURDATE()) AS DaysLeft
    FROM   BloodBag
    WHERE  BagStatus = 0
      AND  ExpiryDate <= DATE_ADD(CURDATE(), INTERVAL 7 DAY)
    ORDER  BY ExpiryDate ASC;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 15: อัปเดตถุงเลือด
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_update_blood_bag;
DELIMITER //
CREATE PROCEDURE sp_update_blood_bag(
    IN p_bagId          INT,
    IN p_donationId     INT,
    IN p_bloodGroup     VARCHAR(2),
    IN p_rhFactor       CHAR(1),
    IN p_collectionDate DATE,
    IN p_expiryDate     DATE
)
BEGIN
    UPDATE BloodBag
    SET    DonationID = p_donationId, BloodGroup = p_bloodGroup,
           RhFactor = p_rhFactor, CollectionDate = p_collectionDate,
           ExpiryDate = p_expiryDate
    WHERE  BagID = p_bagId;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 16: บันทึกรับบริจาค
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_record_donation;
DELIMITER //
CREATE PROCEDURE sp_record_donation(
    IN  p_donationDate DATE,
    IN  p_volume       INT,
    IN  p_donorId      INT,
    IN  p_employeeId   INT,
    OUT p_donationId   INT
)
BEGIN
    INSERT INTO Donation (DonationDate, NextEligibleDate, Volume, DonorID, EmployeeID)
    VALUES (p_donationDate, ADDDATE(p_donationDate, INTERVAL 90 DAY), p_volume, p_donorId, p_employeeId);
    SET p_donationId = LAST_INSERT_ID();
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 17: บันทึกผลทางห้องปฏิบัติการ
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_record_blood_test;
DELIMITER //
CREATE PROCEDURE sp_record_blood_test(
    IN  p_infectiousDiseaseResult VARCHAR(100),
    IN  p_confirmatoryABO         VARCHAR(2),
    IN  p_confirmatoryRh          CHAR(1),
    IN  p_testDate                DATE,
    IN  p_bagId                   INT,
    OUT p_testId                  INT
)
BEGIN
    START TRANSACTION;

    INSERT INTO BloodTest (InfectiousDiseaseResult, ConfirmatoryABO, ConfirmatoryRh, TestDate, BagID)
    VALUES (p_infectiousDiseaseResult, p_confirmatoryABO, p_confirmatoryRh, p_testDate, p_bagId);

    SET p_testId = LAST_INSERT_ID();

    UPDATE BloodBag
    SET    BagStatus = 0
    WHERE  BagID = p_bagId
      AND  EXISTS (
               SELECT 1 FROM BloodTest
               WHERE  BagID = p_bagId AND InfectiousDiseaseResult = 'Negative All'
           );

    COMMIT;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 18: บันทึกการจ่ายเลือด (Trigger จัดการ Function 19)
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_record_blood_usage;
DELIMITER //
CREATE PROCEDURE sp_record_blood_usage(
    IN  p_usageDate  DATE,
    IN  p_patientId  INT,
    IN  p_bagId      INT,
    IN  p_employeeId INT,
    OUT p_usageId    INT
)
BEGIN
    INSERT INTO BloodUsage (UsageDate, PatientID, BagID, EmployeeID)
    VALUES (p_usageDate, p_patientId, p_bagId, p_employeeId);
    SET p_usageId = LAST_INSERT_ID();
    -- Trigger after_blood_usage_insert จะอัปเดต BagStatus → 2 อัตโนมัติ
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 20: บันทึกการระงับสิทธิ์
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_record_deferral;
DELIMITER //
CREATE PROCEDURE sp_record_deferral(
    IN  p_deferralType   VARCHAR(20),
    IN  p_reasonCategory VARCHAR(100),
    IN  p_startDate      DATE,
    IN  p_donorId        INT,
    IN  p_employeeId     INT,
    OUT p_deferralId     INT
)
BEGIN
    START TRANSACTION;

    INSERT INTO Deferral (DeferralType, ReasonCategory, StartDate, EndDate, DonorID, EmployeeID)
    VALUES (p_deferralType, p_reasonCategory, p_startDate,
            DATE_ADD(p_startDate, INTERVAL 14 DAY), p_donorId, p_employeeId);

    SET p_deferralId = LAST_INSERT_ID();

    UPDATE Donor SET Status = 0 WHERE DonorID = p_donorId;

    COMMIT;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 21: Dashboard ยอดรับเข้ารายเดือน
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_dashboard_volume_in;
DELIMITER //
CREATE PROCEDURE sp_dashboard_volume_in(IN p_month INT, IN p_year INT)
BEGIN
    SELECT COALESCE(SUM(dn.Volume), 0) AS TotalVolumeIn
    FROM   BloodBag bb
    JOIN   Donation dn ON bb.DonationID = dn.DonationID
    WHERE  MONTH(bb.CollectionDate) = p_month
      AND  YEAR(bb.CollectionDate)  = p_year;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 22: Dashboard ยอดใช้รายเดือน
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_dashboard_volume_used;
DELIMITER //
CREATE PROCEDURE sp_dashboard_volume_used(IN p_month INT, IN p_year INT)
BEGIN
    SELECT COALESCE(SUM(dn.Volume), 0) AS TotalVolumeUsed
    FROM   BloodUsage bu
    JOIN   BloodBag bb  ON bu.BagID     = bb.BagID
    JOIN   Donation dn  ON bb.DonationID = dn.DonationID
    WHERE  MONTH(bu.UsageDate) = p_month
      AND  YEAR(bu.UsageDate)  = p_year;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 23: Dashboard ยอดสูญเสียรายเดือน (BagStatus=3)
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_dashboard_volume_lost;
DELIMITER //
CREATE PROCEDURE sp_dashboard_volume_lost(IN p_month INT, IN p_year INT)
BEGIN
    SELECT COALESCE(SUM(dn.Volume), 0) AS TotalVolumeLost
    FROM   BloodBag bb
    JOIN   Donation dn ON bb.DonationID = dn.DonationID
    WHERE  bb.BagStatus = 3
      AND  MONTH(bb.CollectionDate) = p_month
      AND  YEAR(bb.CollectionDate)  = p_year;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Function 24: ตารางรายงานการรับบริจาครายเดือน
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_donation_report;
DELIMITER //
CREATE PROCEDURE sp_donation_report(IN p_month INT, IN p_year INT)
BEGIN
    SELECT
        dn.DonationDate                                                             AS donationDate,
        GROUP_CONCAT(DISTINCT d.BloodGroup ORDER BY d.BloodGroup SEPARATOR ', ')   AS bloodGroups,
        COUNT(dn.DonationID)                                                        AS totalCases,
        SUM(dn.Volume)                                                              AS totalVolume,
        SUM(CASE WHEN dn.Volume > 0 THEN 1 ELSE 0 END)                             AS passCount,
        SUM(CASE WHEN dn.Volume = 0 THEN 1 ELSE 0 END)                             AS deferredCount
    FROM  Donation dn
    JOIN  Donor d ON dn.DonorID = d.DonorID
    WHERE MONTH(dn.DonationDate) = p_month
      AND YEAR(dn.DonationDate)  = p_year
    GROUP BY dn.DonationDate
    ORDER BY dn.DonationDate ASC;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Helper: ค้นหา Donor by ID (ใช้ภายใน Java DAO)
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_find_donor_by_id;
DELIMITER //
CREATE PROCEDURE sp_find_donor_by_id(IN p_donorId INT)
BEGIN
    SELECT DonorID, Name, NationalID, Gender, Birthday, Status,
           Remark, BloodGroup, RhFactor, CongenitalDisease
    FROM   Donor WHERE DonorID = p_donorId;
END //
DELIMITER ;

-- ════════════════════════════════════════════════════════════
--  Helper: ค้นหา Patient by ID (ใช้ภายใน Java DAO)
-- ════════════════════════════════════════════════════════════
DROP PROCEDURE IF EXISTS sp_find_patient_by_id;
DELIMITER //
CREATE PROCEDURE sp_find_patient_by_id(IN p_patientId INT)
BEGIN
    SELECT PatientID, NationalID, Name, Gender, BloodGroup, RhFactor,
           Birthday, TransfusionStatus
    FROM   Patient WHERE PatientID = p_patientId;
END //
DELIMITER ;
