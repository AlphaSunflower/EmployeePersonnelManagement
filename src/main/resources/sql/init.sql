-- MySQL dump 10.13  Distrib 5.7.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: employee_personnel
-- ------------------------------------------------------
-- Server version	8.0.31
create database if not exists employee_personnel;
use employee_personnel;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `announcement`
--

DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `publisher_id` bigint NOT NULL,
  `publish_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint DEFAULT '1' COMMENT '1=published, 0=draft',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Announcement';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcement`
--

LOCK TABLES `announcement` WRITE;
/*!40000 ALTER TABLE `announcement` DISABLE KEYS */;
/*!40000 ALTER TABLE `announcement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attendance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_id` bigint NOT NULL COMMENT 'Employee ID',
  `date` date NOT NULL COMMENT 'Attendance date',
  `check_in` datetime DEFAULT NULL COMMENT 'Check-in time',
  `check_out` datetime DEFAULT NULL COMMENT 'Check-out time',
  `wifi_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'WiFi name used',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'NORMAL' COMMENT 'NORMAL, LATE, EARLY, ABSENT',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_emp_date` (`employee_id`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Attendance record';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (1,24,'2026-06-05','2026-06-05 23:04:45','2026-06-05 23:40:58','CompanyWiFi-5G','LATE','2026-06-05 23:04:45','2026-06-05 23:04:45'),(2,25,'2026-06-07','2026-06-07 11:53:55','2026-06-07 12:10:31','CompanyWiFi-5G','LATE','2026-06-07 11:53:55','2026-06-07 11:53:55');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Department name',
  `parent_id` bigint DEFAULT '0' COMMENT 'Parent department ID, 0 for root',
  `sort_order` int DEFAULT '0' COMMENT 'Sort order',
  `status` tinyint DEFAULT '1' COMMENT '1=active, 0=inactive',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0' COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Department';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'总公司',0,1,1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(2,'技术部',1,2,1,'2026-06-05 22:04:55','2026-06-05 23:56:48',0),(3,'人事部',1,3,1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(4,'财务部',1,4,1,'2026-06-05 22:04:55','2026-06-05 23:57:00',0),(5,'市场部',1,5,1,'2026-06-05 22:04:55','2026-06-05 23:56:57',0);
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Employee name',
  `gender` tinyint DEFAULT NULL COMMENT '0=female, 1=male',
  `id_card` varchar(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ID card number',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Phone',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Email',
  `hire_date` date DEFAULT NULL COMMENT 'Hire date',
  `dept_id` bigint DEFAULT NULL COMMENT 'Department ID',
  `position_id` bigint DEFAULT NULL COMMENT 'Position ID',
  `level` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Job level',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE' COMMENT 'ACTIVE, PROBATION, RESIGNED',
  `avatar_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Avatar URL (OSS)',
  `emergency_contact` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Emergency contact name',
  `emergency_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Emergency contact phone',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Employee';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (17,'张三',1,'310101199001011234','13800001001','zhangsan@company.com','2023-01-15',2,3,'P6','ACTIVE',NULL,NULL,NULL,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(18,'李四',1,'310101199205052345','13800001002','lisi@company.com','2023-06-01',2,4,'P5','ACTIVE',NULL,NULL,NULL,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(19,'王五',0,'310101198803033456','13800001003','wangwu@company.com','2022-03-20',3,5,'M2','ACTIVE',NULL,NULL,NULL,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(20,'赵六',0,'310101199507074567','13800001004','zhaoliu@company.com','2023-09-10',3,6,'P4','ACTIVE',NULL,NULL,NULL,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(21,'孙七',1,'310101198507085678','13800001005','sunqi@company.com','2021-07-01',4,7,'M2','ACTIVE',NULL,NULL,NULL,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(22,'周八',0,'310101199610106789','13800001006','zhouba@company.com','2024-02-15',4,8,'P3','ACTIVE',NULL,NULL,NULL,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(23,'吴九',1,'310101199212127890','13800001007','wujiu@company.com','2023-04-01',5,9,'M1','ACTIVE',NULL,NULL,NULL,'2026-06-05 22:04:55','2026-06-05 23:50:14',0),(24,'郑十',0,'310101199808088901','13800001008','zhengshi@company.com','2024-05-20',5,10,'P2','PROBATION',NULL,NULL,NULL,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(25,'魏辰益',1,'441424111111111111','13425244882','1111111111@qq.com','2026-06-06',2,4,'','ACTIVE','/uploads/avatars/11c06d8c.jpg',NULL,NULL,'2026-06-07 11:04:29','2026-06-07 13:47:32',0);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_salary`
--

DROP TABLE IF EXISTS `employee_salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_salary` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_id` bigint NOT NULL,
  `structure_id` bigint DEFAULT NULL,
  `basic_salary` decimal(10,2) NOT NULL DEFAULT '0.00',
  `performance_salary` decimal(10,2) NOT NULL DEFAULT '0.00',
  `subsidy` decimal(10,2) NOT NULL DEFAULT '0.00',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Employee salary standard';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_salary`
--

LOCK TABLES `employee_salary` WRITE;
/*!40000 ALTER TABLE `employee_salary` DISABLE KEYS */;
INSERT INTO `employee_salary` VALUES (24,17,NULL,5000.00,2000.00,500.00,'2026-06-05 22:05:27','2026-06-05 22:05:27'),(25,18,NULL,5000.00,2000.00,500.00,'2026-06-05 22:05:27','2026-06-05 22:05:27'),(26,19,NULL,15000.00,7000.00,500.00,'2026-06-05 22:05:27','2026-06-05 22:05:27'),(27,20,NULL,5000.00,2000.00,500.00,'2026-06-05 22:05:27','2026-06-05 22:05:27'),(28,21,NULL,15000.00,7000.00,500.00,'2026-06-05 22:05:27','2026-06-05 22:05:27'),(29,22,NULL,5000.00,2000.00,500.00,'2026-06-05 22:05:27','2026-06-05 22:05:27'),(30,23,NULL,15000.00,7000.00,500.00,'2026-06-05 22:05:27','2026-06-05 22:05:27'),(31,24,NULL,5000.00,2000.00,500.00,'2026-06-05 22:05:27','2026-06-05 22:05:27'),(49,25,NULL,12222.00,0.00,0.00,'2026-06-07 11:06:14','2026-06-07 11:06:14');
/*!40000 ALTER TABLE `employee_salary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_request`
--

DROP TABLE IF EXISTS `leave_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leave_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_id` bigint NOT NULL COMMENT 'Employee ID',
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ANNUAL, SICK, PERSONAL',
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `days` decimal(3,1) NOT NULL COMMENT 'Leave days',
  `reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Leave reason',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, REJECTED',
  `approver_id` bigint DEFAULT NULL COMMENT 'Approver (admin) ID',
  `approve_time` datetime DEFAULT NULL COMMENT 'Approve time',
  `approve_comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Approve comment',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Leave request';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_request`
--

LOCK TABLES `leave_request` WRITE;
/*!40000 ALTER TABLE `leave_request` DISABLE KEYS */;
INSERT INTO `leave_request` VALUES (1,24,'PERSONAL','2026-06-04','2026-06-04',1.0,'肚子疼','APPROVED',1,'2026-06-05 23:44:53','','2026-06-05 23:40:53','2026-06-05 23:44:53');
/*!40000 ALTER TABLE `leave_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `makeup_request`
--

DROP TABLE IF EXISTS `makeup_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `makeup_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_id` bigint NOT NULL,
  `date` date NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'CHECK_IN, CHECK_OUT',
  `reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `approver_id` bigint DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL,
  `approve_comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Makeup punch request';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `makeup_request`
--

LOCK TABLES `makeup_request` WRITE;
/*!40000 ALTER TABLE `makeup_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `makeup_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `overtime_request`
--

DROP TABLE IF EXISTS `overtime_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `overtime_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_id` bigint NOT NULL,
  `date` date NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `hours` decimal(4,1) NOT NULL COMMENT 'Overtime hours',
  `reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `approver_id` bigint DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL,
  `approve_comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Overtime request';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `overtime_request`
--

LOCK TABLES `overtime_request` WRITE;
/*!40000 ALTER TABLE `overtime_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `overtime_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `position` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Position name',
  `dept_id` bigint DEFAULT NULL COMMENT 'Department ID',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Position description',
  `status` tinyint DEFAULT '1' COMMENT '1=active, 0=inactive',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Position';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position`
--

LOCK TABLES `position` WRITE;
/*!40000 ALTER TABLE `position` DISABLE KEYS */;
INSERT INTO `position` VALUES (1,'总经理',1,'公司总经理',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(2,'技术总监',2,'技术部负责人',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(3,'高级开发工程师',2,'高级软件开发',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(4,'开发工程师',2,'软件开发',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(5,'人事经理',3,'人事部负责人',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(6,'人事专员',3,'人事事务处理',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(7,'财务经理',4,'财务部负责人',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(8,'会计',4,'财务会计',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(9,'市场经理',5,'市场部负责人',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0),(10,'市场专员',5,'市场推广',1,'2026-06-05 22:04:55','2026-06-05 22:04:55',0);
/*!40000 ALTER TABLE `position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporting_relationship`
--

DROP TABLE IF EXISTS `reporting_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reporting_relationship` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_id` bigint NOT NULL COMMENT 'Employee ID',
  `supervisor_id` bigint NOT NULL COMMENT 'Direct supervisor ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Reporting relationship';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporting_relationship`
--

LOCK TABLES `reporting_relationship` WRITE;
/*!40000 ALTER TABLE `reporting_relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `reporting_relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary_record`
--

DROP TABLE IF EXISTS `salary_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `salary_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_id` bigint NOT NULL,
  `year` int NOT NULL,
  `month` int NOT NULL,
  `basic_salary` decimal(10,2) DEFAULT '0.00',
  `performance_salary` decimal(10,2) DEFAULT '0.00',
  `subsidy` decimal(10,2) DEFAULT '0.00',
  `attendance_deduction` decimal(10,2) DEFAULT '0.00',
  `social_insurance_personal` decimal(10,2) DEFAULT '0.00',
  `housing_fund_personal` decimal(10,2) DEFAULT '0.00',
  `taxable_income` decimal(10,2) DEFAULT '0.00',
  `tax` decimal(10,2) DEFAULT '0.00',
  `net_salary` decimal(10,2) DEFAULT '0.00' COMMENT 'Actual net salary',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'DRAFT' COMMENT 'DRAFT, CONFIRMED',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_emp_ym` (`employee_id`,`year`,`month`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Monthly salary record';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary_record`
--

LOCK TABLES `salary_record` WRITE;
/*!40000 ALTER TABLE `salary_record` DISABLE KEYS */;
INSERT INTO `salary_record` VALUES (1,17,2026,6,14000.00,5000.00,500.00,0.00,2047.50,1365.00,11087.50,898.75,15188.75,'CONFIRMED','2026-06-05 22:08:54','2026-06-05 22:08:54'),(2,18,2026,6,12500.00,4000.00,500.00,0.00,1785.00,1190.00,9025.00,692.50,13332.50,'CONFIRMED','2026-06-05 22:08:54','2026-06-05 22:08:54'),(3,19,2026,6,18000.00,7000.00,500.00,0.00,2677.50,1785.00,16037.50,1797.50,19240.00,'CONFIRMED','2026-06-05 22:08:54','2026-06-05 22:08:54'),(4,20,2026,6,11000.00,3500.00,500.00,0.00,1575.00,1050.00,7375.00,527.50,11847.50,'CONFIRMED','2026-06-05 22:08:54','2026-06-05 22:08:54'),(5,21,2026,6,18000.00,7000.00,500.00,0.00,2677.50,1785.00,16037.50,1797.50,19240.00,'CONFIRMED','2026-06-05 22:08:54','2026-06-05 22:08:54'),(6,22,2026,6,9500.00,3000.00,500.00,0.00,1365.00,910.00,5725.00,362.50,10362.50,'CONFIRMED','2026-06-05 22:08:54','2026-06-05 22:08:54'),(7,23,2026,6,16000.00,5000.00,500.00,0.00,2257.50,1505.00,12737.50,1137.50,16600.00,'CONFIRMED','2026-06-05 22:08:54','2026-06-05 22:08:54'),(8,24,2026,6,8000.00,2500.00,500.00,555.75,1155.00,770.00,3519.25,141.93,8377.32,'CONFIRMED','2026-06-05 22:08:54','2026-06-05 22:08:54');
/*!40000 ALTER TABLE `salary_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary_structure`
--

DROP TABLE IF EXISTS `salary_structure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `salary_structure` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Structure name',
  `basic_salary` decimal(10,2) DEFAULT '0.00' COMMENT 'Default basic salary',
  `performance_salary` decimal(10,2) DEFAULT '0.00' COMMENT 'Default performance salary',
  `subsidy` decimal(10,2) DEFAULT '0.00' COMMENT 'Default subsidy',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Salary structure template';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary_structure`
--

LOCK TABLES `salary_structure` WRITE;
/*!40000 ALTER TABLE `salary_structure` DISABLE KEYS */;
INSERT INTO `salary_structure` VALUES (1,'标准薪酬结构',5000.00,2000.00,500.00,'默认薪酬结构','2026-06-05 21:26:30','2026-06-05 21:26:30',0);
/*!40000 ALTER TABLE `salary_structure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Username',
  `password` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'BCrypt encoded password',
  `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'EMPLOYEE' COMMENT 'EMPLOYEE, ADMIN',
  `employee_id` bigint DEFAULT NULL COMMENT 'Linked employee ID',
  `status` tinyint DEFAULT '1' COMMENT '1=active, 0=disabled',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System user';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (35,'13800001001','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','ADMIN',17,1,'2026-06-05 22:05:27','2026-06-05 23:03:31',0),(36,'13800001002','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','EMPLOYEE',18,1,'2026-06-05 22:05:27','2026-06-05 23:04:17',0),(37,'13800001003','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','EMPLOYEE',19,1,'2026-06-05 22:05:27','2026-06-05 23:04:17',0),(38,'13800001004','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','EMPLOYEE',20,1,'2026-06-05 22:05:27','2026-06-05 23:04:17',0),(39,'13800001005','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','EMPLOYEE',21,1,'2026-06-05 22:05:27','2026-06-05 23:04:17',0),(40,'13800001006','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','EMPLOYEE',22,1,'2026-06-05 22:05:27','2026-06-05 23:04:17',0),(41,'13800001007','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','EMPLOYEE',23,1,'2026-06-05 22:05:27','2026-06-05 23:04:17',0),(42,'13800001008','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','EMPLOYEE',24,1,'2026-06-05 22:05:27','2026-06-05 23:04:17',0),(51,'admin','$2a$10$Z1vi4u54pvnv9FDJLGVRiuTWCZHperKMQXo2UPqei4DizHJZtpPAy','ADMIN',NULL,1,'2026-06-05 22:25:48','2026-06-05 22:25:48',0),(52,'13425244882','$2a$10$BOpldnVOjd6021GY/PKNzO6N3qqemOIFg/bpwOpvQt0A0rYBZ.ET2','EMPLOYEE',25,1,'2026-06-07 11:04:29','2026-06-07 11:04:29',0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-11  0:27:43
