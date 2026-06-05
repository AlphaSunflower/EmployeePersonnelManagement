-- Employee Personnel Management System - Database Initialization
-- Database: employee_personnel

CREATE DATABASE IF NOT EXISTS employee_personnel DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE employee_personnel;

-- 1. Department table
CREATE TABLE IF NOT EXISTS department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT 'Department name',
    parent_id BIGINT DEFAULT 0 COMMENT 'Parent department ID, 0 for root',
    sort_order INT DEFAULT 0 COMMENT 'Sort order',
    status TINYINT DEFAULT 1 COMMENT '1=active, 0=inactive',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0 COMMENT 'Logical delete flag'
) COMMENT 'Department';

-- 2. Position table
CREATE TABLE IF NOT EXISTS position (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT 'Position name',
    dept_id BIGINT COMMENT 'Department ID',
    description VARCHAR(500) COMMENT 'Position description',
    status TINYINT DEFAULT 1 COMMENT '1=active, 0=inactive',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT 'Position';

-- 3. Employee table
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT 'Employee name',
    gender TINYINT COMMENT '0=female, 1=male',
    id_card VARCHAR(18) COMMENT 'ID card number',
    phone VARCHAR(20) COMMENT 'Phone',
    email VARCHAR(100) COMMENT 'Email',
    hire_date DATE COMMENT 'Hire date',
    dept_id BIGINT COMMENT 'Department ID',
    position_id BIGINT COMMENT 'Position ID',
    level VARCHAR(50) COMMENT 'Job level',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, PROBATION, RESIGNED',
    avatar_url VARCHAR(500) COMMENT 'Avatar URL (OSS)',
    emergency_contact VARCHAR(50) COMMENT 'Emergency contact name',
    emergency_phone VARCHAR(20) COMMENT 'Emergency contact phone',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT 'Employee';

-- 4. System user table
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Username',
    password VARCHAR(200) NOT NULL COMMENT 'BCrypt encoded password',
    role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE' COMMENT 'EMPLOYEE, ADMIN',
    employee_id BIGINT COMMENT 'Linked employee ID',
    status TINYINT DEFAULT 1 COMMENT '1=active, 0=disabled',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT 'System user';

-- 5. Reporting relationship table
CREATE TABLE IF NOT EXISTS reporting_relationship (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL COMMENT 'Employee ID',
    supervisor_id BIGINT NOT NULL COMMENT 'Direct supervisor ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT 'Reporting relationship';

-- 6. Attendance table
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL COMMENT 'Employee ID',
    date DATE NOT NULL COMMENT 'Attendance date',
    check_in DATETIME COMMENT 'Check-in time',
    check_out DATETIME COMMENT 'Check-out time',
    wifi_name VARCHAR(100) COMMENT 'WiFi name used',
    status VARCHAR(20) DEFAULT 'NORMAL' COMMENT 'NORMAL, LATE, EARLY, ABSENT',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_emp_date (employee_id, date)
) COMMENT 'Attendance record';

-- 7. Leave request table
CREATE TABLE IF NOT EXISTS leave_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL COMMENT 'Employee ID',
    type VARCHAR(20) NOT NULL COMMENT 'ANNUAL, SICK, PERSONAL',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    days DECIMAL(3,1) NOT NULL COMMENT 'Leave days',
    reason VARCHAR(500) COMMENT 'Leave reason',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, REJECTED',
    approver_id BIGINT COMMENT 'Approver (admin) ID',
    approve_time DATETIME COMMENT 'Approve time',
    approve_comment VARCHAR(500) COMMENT 'Approve comment',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT 'Leave request';

-- 8. Overtime request table
CREATE TABLE IF NOT EXISTS overtime_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    date DATE NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    hours DECIMAL(4,1) NOT NULL COMMENT 'Overtime hours',
    reason VARCHAR(500),
    status VARCHAR(20) DEFAULT 'PENDING',
    approver_id BIGINT,
    approve_time DATETIME,
    approve_comment VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT 'Overtime request';

-- 9. Makeup (missed punch) request table
CREATE TABLE IF NOT EXISTS makeup_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    date DATE NOT NULL,
    type VARCHAR(20) NOT NULL COMMENT 'CHECK_IN, CHECK_OUT',
    reason VARCHAR(500),
    status VARCHAR(20) DEFAULT 'PENDING',
    approver_id BIGINT,
    approve_time DATETIME,
    approve_comment VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT 'Makeup punch request';

-- 10. Salary structure table
CREATE TABLE IF NOT EXISTS salary_structure (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT 'Structure name',
    basic_salary DECIMAL(10,2) DEFAULT 0 COMMENT 'Default basic salary',
    performance_salary DECIMAL(10,2) DEFAULT 0 COMMENT 'Default performance salary',
    subsidy DECIMAL(10,2) DEFAULT 0 COMMENT 'Default subsidy',
    description VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT 'Salary structure template';

-- 11. Employee salary table
CREATE TABLE IF NOT EXISTS employee_salary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    structure_id BIGINT,
    basic_salary DECIMAL(10,2) NOT NULL DEFAULT 0,
    performance_salary DECIMAL(10,2) NOT NULL DEFAULT 0,
    subsidy DECIMAL(10,2) NOT NULL DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT 'Employee salary standard';

-- 12. Salary record table (monthly payroll)
CREATE TABLE IF NOT EXISTS salary_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    year INT NOT NULL,
    month INT NOT NULL,
    basic_salary DECIMAL(10,2) DEFAULT 0,
    performance_salary DECIMAL(10,2) DEFAULT 0,
    subsidy DECIMAL(10,2) DEFAULT 0,
    attendance_deduction DECIMAL(10,2) DEFAULT 0,
    social_insurance_personal DECIMAL(10,2) DEFAULT 0,
    housing_fund_personal DECIMAL(10,2) DEFAULT 0,
    taxable_income DECIMAL(10,2) DEFAULT 0,
    tax DECIMAL(10,2) DEFAULT 0,
    net_salary DECIMAL(10,2) DEFAULT 0 COMMENT 'Actual net salary',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT 'DRAFT, CONFIRMED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_emp_ym (employee_id, year, month)
) COMMENT 'Monthly salary record';

-- 13. Announcement table
CREATE TABLE IF NOT EXISTS announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    publisher_id BIGINT NOT NULL,
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TINYINT DEFAULT 1 COMMENT '1=published, 0=draft',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT 'Announcement';

-- Seed data: Default departments
INSERT INTO department (name, parent_id, sort_order) VALUES
('总公司', 0, 1),
('技术部', 1, 2),
('人事部', 1, 3),
('财务部', 1, 4),
('市场部', 1, 5);

-- Seed data: Default positions
INSERT INTO position (name, dept_id, description) VALUES
('总经理', 1, '公司总经理'),
('技术总监', 2, '技术部负责人'),
('高级开发工程师', 2, '高级软件开发'),
('开发工程师', 2, '软件开发'),
('人事经理', 3, '人事部负责人'),
('人事专员', 3, '人事事务处理'),
('财务经理', 4, '财务部负责人'),
('会计', 4, '财务会计'),
('市场经理', 5, '市场部负责人'),
('市场专员', 5, '市场推广');


-- Seed data: Default salary structure
INSERT INTO salary_structure (name, basic_salary, performance_salary, subsidy, description) VALUES
    ('标准薪酬结构', 5000, 2000, 500, '默认薪酬结构');

-- Seed admin user (password: admin123, BCrypt encoded)
INSERT INTO sys_user (username, password, role, employee_id, status) VALUES
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', NULL, 1);
