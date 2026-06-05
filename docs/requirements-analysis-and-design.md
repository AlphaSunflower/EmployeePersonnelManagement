# 员工人事管理系统 — 需求分析与设计方案

> 日期: 2026-06-05 | 版本: v1.0

## 摘要

基于 Spring Boot 3.5.14 + Vue 3 构建员工人事管理系统，包含六大功能模块。后端通过 Spring Security + JWT 统一鉴权，MyBatis-Plus 做 ORM，阿里云 OSS 存储头像，Apache POI 导出 Excel。前端 Vue 3 + Element Plus，角色路由分离（员工门户 vs 管理后台）。

## 系统模块总览

| 模块 | 后端模块 | 前端路由前缀 |
|------|:--:|:--:|
| 一、登录与鉴权 | auth | /login |
| 二、组织架构管理 | organization | /admin/* |
| 三、核心人事档案 | employee | /admin/* + /employee/* |
| 四、考勤与请假 | attendance | /admin/* + /employee/* |
| 五、薪酬核算 | salary | /admin/* |
| 六、员工自助 | 无独立后端 | /employee/*（聚合视图） |

## 一、系统架构

```
+-------------------------------------------------------+
|                   前端 (Vue 3 + Vite)                  |
|  +----------+ +----------+ +----------+ +-----------+ |
|  | 员工门户  | | 管理后台  | | 登录页   | | 公共组件库| |
|  | (模块六)  | | (模块2-5) | | (模块一)  | |          | |
|  +----------+ +----------+ +----------+ +-----------+ |
|         Element Plus  .  ECharts  .  Axios             |
+-------------------+-----------------------------------+
                    |  /api/*  (Vite proxy)
+-------------------+-----------------------------------+
|                后端 (Spring Boot 3.5)                  |
|  +----------+ +----------+ +------------------------+ |
|  | Security | |Controller| |   Service Layer        | |
|  |JWT Filter| |  Layer   | |   (业务逻辑)            | |
|  +----------+ +----------+ +------------------------+ |
|  +--------------------------------------------------+ |
|  |              MyBatis-Plus Mapper Layer            | |
|  +--------------------------------------------------+ |
|  +----------+ +----------+ +----------------------+  |
|  | OSS SDK  | |  Hutool  | |  Apache POI (Excel)  |  |
|  +----------+ +----------+ +----------------------+  |
+-------------------+-----------------------------------+
                    |
             +------+------+
             |  MySQL 8.x  |
             +-------------+
```

## 二、数据库设计

### 表结构（13 张表）

| 表 | 核心字段 |
|---|---|
| sys_user | id, username, password(BCrypt), role(EMPLOYEE/ADMIN), employee_id, status |
| employee | id, name, gender, id_card, phone, email, hire_date, dept_id, position_id, level, status, avatar_url, emergency_contact, emergency_phone |
| department | id, name, parent_id, sort_order, status |
| position | id, name, dept_id, description, status |
| reporting_relationship | id, employee_id, supervisor_id |
| attendance | id, employee_id, date, check_in, check_out, wifi_name, status(NORMAL/LATE/EARLY/ABSENT) |
| leave_request | id, employee_id, type(ANNUAL/SICK/PERSONAL), start_date, end_date, days, reason, status(PENDING/APPROVED/REJECTED), approver_id, approve_time, approve_comment |
| overtime_request | id, employee_id, date, start_time, end_time, hours, reason, status, approver_id |
| makeup_request | id, employee_id, date, type(CHECK_IN/CHECK_OUT), reason, status, approver_id |
| salary_structure | id, name, basic_salary, performance_salary, subsidy, description |
| employee_salary | id, employee_id, structure_id, basic_salary, performance_salary, subsidy |
| salary_record | id, employee_id, year, month, basic_salary, performance_salary, subsidy, attendance_deduction, social_insurance_personal, housing_fund_personal, taxable_income, tax, net_salary, status |
| announcement | id, title, content, publisher_id, publish_time, status |

## 三、模块详细设计

### 模块一：登录与鉴权

**后端 API：**
- POST /api/auth/login — 接收 username + password，验证后返回 JWT token + 角色 + 员工ID
- GET /api/auth/info — 根据 token 返回当前用户信息（姓名、角色、头像等）

**前端页面：**
- /login — Element Plus 登录表单，登录后按角色跳转
- Axios 拦截器：请求自动带 token，401 时跳转登录页
- Pinia 存储用户状态（localStorage 持久化）

### 模块二：组织架构管理（仅管理员）

**后端 API：**
- GET/POST /api/departments — 部门树查询（递归）、新增部门
- PUT/DELETE /api/departments/{id} — 编辑/删除部门
- GET/POST /api/positions — 岗位列表/新增
- PUT/DELETE /api/positions/{id} — 编辑/删除岗位
- GET /api/reporting-relationships — 查员工上级
- POST /api/reporting-relationships — 设置汇报关系
- GET /api/organization/tree — 完整组织架构树（供 ECharts 使用）

**前端页面：** /admin/departments, /admin/positions, /admin/organization

### 模块三：核心人事档案管理

**管理员 API：**
- GET/POST/PUT/DELETE /api/employees — 员工 CRUD + 分页筛选
- POST /api/employees/{id}/change — 异动操作（转正/调岗/离职）
- GET /api/employees/export — 导出 Excel 花名册

**员工 API：**
- GET/PUT /api/employees/me — 查看/编辑个人档案
- POST /api/employees/me/avatar — 上传头像到 OSS

**前端页面：** /admin/employees, /admin/employees/:id, /employee/profile

### 模块四：考勤与请假管理

**WiFi 模拟打卡：** 前后端约定固定 WiFi 名称 CompanyWiFi-5G，点击按钮即记录

**考勤 API：**
- POST /api/attendance/check-in — 上班打卡
- POST /api/attendance/check-out — 下班打卡
- GET /api/attendance/today — 查询当天打卡状态
- GET /api/attendance/monthly — 个人月度汇总
- GET /api/attendance/monthly/all — 管理员查全员月度汇总

**请假/加班/补签 API：**
- POST/GET /api/leave-requests — 提交/查看请假
- PUT /api/leave-requests/{id}/approve — 审批请假
- 加班和补签同理（/api/overtime-requests, /api/makeup-requests）

**前端页面：** /employee/attendance, /employee/leave, /employee/overtime, /employee/makeup, /admin/attendance, /admin/leave-approval, /admin/overtime-approval, /admin/makeup-approval

### 模块五：薪酬核算管理（仅管理员）

**薪酬公式（简化）：**
```
应发工资 = 基本工资 + 绩效工资 + 津贴补贴
考勤扣款 = (迟到次数 x 50 + 旷工天数 x 200 + 事假天数 x 日工资)
社保个人 = 应发工资 x 10.5%
公积金个人 = 应发工资 x 7%
应纳税所得额 = 应发工资 - 考勤扣款 - 社保个人 - 公积金个人 - 5000
个税 = 应纳税所得额 x 阶梯税率 - 速算扣除数
实发工资 = 应发工资 - 考勤扣款 - 社保个人 - 公积金个人 - 个税
```

**后端 API：**
- GET/POST /api/salary-structures — 薪酬结构 CRUD
- GET/POST/PUT /api/employee-salaries — 员工薪酬标准管理
- POST /api/salary/calculate — 按月核算全员薪酬
- GET /api/salary/records — 查看月度工资表
- GET /api/salary/records/{id}/payslip — 工资条详情
- GET /api/salary/records/export — 导出月度工资 Excel
- GET /api/salary/records/me — 员工查自己的工资条

**前端页面：** /admin/salary-structures, /admin/employee-salaries, /admin/salary-calculate, /admin/salary-records, /employee/salary

### 模块六：员工自助服务

前端聚合视图，整合模块三/四/五中员工可操作的功能：
- /employee/dashboard — 员工首页概览
- /employee/profile — 个人信息
- /employee/attendance — 考勤打卡与查询
- /employee/leave — 请假管理
- /employee/overtime — 加班申请
- /employee/makeup — 补签申请
- /employee/salary — 工资条

## 四、前端项目结构

```
frontend/src/
  api/            # Axios 接口封装（按模块分文件）
  router/         # 路由配置 + 守卫
  stores/         # Pinia 状态管理
  utils/          # request.js Axios 实例
  views/
    login/
    employee/     # 员工门户页面
    admin/        # 管理后台页面
  layouts/        # EmployeeLayout / AdminLayout
  App.vue
```

## 五、实施顺序

1. 后端基础设施 — pom.xml、application.yaml、init.sql、公共组件
2. 前端脚手架 — Vite 项目、路由、Axios、布局、登录页
3. 模块一 — 登录鉴权前后端联调
4. 模块二 — 组织架构管理
5. 模块三 — 人事档案 + OSS + Excel
6. 模块四 — 考勤打卡 + 请假/加班/补签 + 审批
7. 模块五 — 薪酬核算 + 工资条 + Excel
8. 模块六 — 员工门户整合

## 六、假设与约定

- WiFi 打卡：模拟实现，固定 WiFi 名称 CompanyWiFi-5G
- 薪酬公式：固定简化公式，五险一金比例写死在常量中
- 头像上传：前端 Upload -> 后端中转 -> OSS 存储
- Excel 导出：Apache POI 服务端生成 .xlsx
- 角色：仅 EMPLOYEE 和 ADMIN 两种
- 数据库名：employee_personnel
- init.sql 位置：src/main/resources/sql/
- 前端目录：frontend/（与 src/ 同级）
