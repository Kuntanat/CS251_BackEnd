-- ============================================================
--  CS251 Blood Bank - Sample Data
--  สำหรับ development / testing เท่านั้น
-- ============================================================

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cs251_db;

-- ────────────────────────────────────────────────────────────
--  Employee
-- ────────────────────────────────────────────────────────────
INSERT INTO Employee (EmployeeID, Name, Role, Birthday) VALUES
(8001, 'Somchai Jordan',   'Doctor',          '1985-03-15'),
(8002, 'Malee Srisuk',     'Nurse',           '1990-07-22'),
(8003, 'Prasert Tanaka',   'Lab Technician',  '1988-11-05'),
(8004, 'Sunisa Wongchai',  'Nurse',           '1993-04-18');

-- ────────────────────────────────────────────────────────────
--  EmployeeContact
-- ────────────────────────────────────────────────────────────
INSERT INTO EmployeeContact (ContactType, ContactValue, EmployeeID) VALUES
('Email', 'somchai.jordan@hospital.com', 8001),
('Phone', '0812345678',                  8001),
('Email', 'malee.srisuk@hospital.com',  8002),
('Email', 'prasert.t@hospital.com',     8003);

-- ────────────────────────────────────────────────────────────
--  Donor
-- ────────────────────────────────────────────────────────────
INSERT INTO Donor (DonorID, Name, NationalID, Gender, Birthday, Status, Remark, BloodGroup, RhFactor, CongenitalDisease) VALUES
(1001, 'Vichai Smith',     '1100112345678', 'M', '1990-05-01', 1, NULL,                    'AB', '+', NULL),
(1002, 'Nipa Kamolrat',    '3400223456789', 'F', '1995-08-15', 1, NULL,                    'A',  '+', NULL),
(1003, 'Wanchai Buasri',   '2300334567890', 'M', '1988-02-28', 1, 'มีภาวะไม่พึงประสงค์',   'B',  '-', 'Diabetes'),
(1004, 'Ratana Pichit',    '1500445678901', 'F', '2000-11-10', 0, 'ระงับชั่วคราว',          'O',  '+', NULL),
(1005, 'Thana Somboon',    '4100556789012', 'M', '1993-06-25', 1, NULL,                    'A',  '-', NULL);

-- ────────────────────────────────────────────────────────────
--  DonorContact
-- ────────────────────────────────────────────────────────────
INSERT INTO DonorContact (ContactType, ContactValue, DonorID) VALUES
('Email', 'vichai@gmail.com',    1001),
('Phone', '0891112222',          1001),
('Email', 'nipa.k@gmail.com',    1002),
('Phone', '0863334444',          1003),
('Email', 'thana.s@hotmail.com', 1005);

-- ────────────────────────────────────────────────────────────
--  Patient
-- ────────────────────────────────────────────────────────────
INSERT INTO Patient (PatientID, NationalID, Name, Gender, BloodGroup, RhFactor, Birthday, TransfusionStatus) VALUES
(9001, '1300011122235', 'Malee Jan',       'F', 'A',  '+', '1975-04-12', 'จองเลือดล่วงหน้า'),
(9002, '2500022233346', 'Somsak Decha',    'M', 'O',  '+', '1960-09-30', 'ขอรับเลือดด่วน'),
(9003, '3700033344457', 'Pranee Chumdee',  'F', 'B',  '-', '1982-01-17', 'ได้รับเลือดแล้ว');

-- ────────────────────────────────────────────────────────────
--  Account
-- ────────────────────────────────────────────────────────────
-- password: Admin1234  →  verified BCrypt $2a$10$ hash
INSERT INTO Account (Username, Password, UserType, Status, ReferenceID) VALUES
('admin',        '$2a$10$ipeeew.x8KNDnVOaApAs2.H8m9DQlL02Sd/5H6qTmme9cnorhfu3q', 'Employee', 1, 8001),
('malee.nurse',  '$2a$10$ipeeew.x8KNDnVOaApAs2.H8m9DQlL02Sd/5H6qTmme9cnorhfu3q', 'Employee', 1, 8002),
('vichai.donor', '$2a$10$ipeeew.x8KNDnVOaApAs2.H8m9DQlL02Sd/5H6qTmme9cnorhfu3q', 'Donor',    1, 1001),
('nipa.donor',   '$2a$10$ipeeew.x8KNDnVOaApAs2.H8m9DQlL02Sd/5H6qTmme9cnorhfu3q', 'Donor',    1, 1002);

-- ────────────────────────────────────────────────────────────
--  Donation
-- ────────────────────────────────────────────────────────────
INSERT INTO Donation (DonationID, DonationDate, NextEligibleDate, Volume, DonorID, EmployeeID) VALUES
(2001, '2026-01-02', '2026-04-02', 450, 1001, 8001),
(2002, '2026-01-15', '2026-04-15', 450, 1002, 8002),
(2003, '2026-02-10', '2026-05-10', 400, 1003, 8001),
(2004, '2026-03-01', '2026-06-01', 450, 1005, 8002);

-- ────────────────────────────────────────────────────────────
--  BloodBag
-- ────────────────────────────────────────────────────────────
INSERT INTO BloodBag (BagID, ComponentType, BloodGroup, RhFactor, CollectionDate, ExpiryDate, BagStatus, DonationID) VALUES
(5001, 'PRC',      'AB', '+', '2026-01-02', '2026-02-13', 2, 2001),   -- Used
(5002, 'FFP',      'AB', '+', '2026-01-02', '2027-01-02', 0, 2001),   -- Available
(5003, 'PRC',      'A',  '+', '2026-01-15', '2026-02-26', 0, 2002),   -- Available
(5004, 'Platelet', 'B',  '-', '2026-02-10', '2026-02-15', 3, 2003),   -- Disposed
(5005, 'PRC',      'A',  '-', '2026-03-01', '2026-04-12', 0, 2004);   -- Available

-- ────────────────────────────────────────────────────────────
--  BloodTest
-- ────────────────────────────────────────────────────────────
INSERT INTO BloodTest (InfectiousDiseaseResult, ConfirmatoryABO, ConfirmatoryRh, TestDate, BagID) VALUES
('Negative All', 'AB', '+', '2026-01-03', 5001),
('Negative All', 'AB', '+', '2026-01-03', 5002),
('Negative All', 'A',  '+', '2026-01-16', 5003),
('Negative All', 'B',  '-', '2026-02-11', 5004),
('Negative All', 'A',  '-', '2026-03-02', 5005);

-- ────────────────────────────────────────────────────────────
--  Deferral
-- ────────────────────────────────────────────────────────────
INSERT INTO Deferral (DeferralType, ReasonCategory, StartDate, EndDate, DonorID, EmployeeID) VALUES
('Temporary',  'ความดันต่ำ', '2026-02-02', '2026-02-20', 1004, 8001),
('Temporary',  'ซีด',        '2026-03-10', '2026-04-10', 1003, 8002);

-- ────────────────────────────────────────────────────────────
--  BloodUsage
-- ────────────────────────────────────────────────────────────
INSERT INTO BloodUsage (UsageDate, PatientID, BagID, EmployeeID) VALUES
('2026-04-05', 9001, 5001, 8001),
('2026-04-06', 9002, 5003, 8001);

-- ────────────────────────────────────────────────────────────
--  Disposal
-- ────────────────────────────────────────────────────────────
INSERT INTO Disposal (DisposalDate, Reason, BagID, EmployeeID) VALUES
('2026-02-16', 'หมดอายุ',   5004, 8003),
('2026-04-08', 'ปนเปื้อน',  5005, 8003);
