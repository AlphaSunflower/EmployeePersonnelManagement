# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

员工人事管理系统，员工/管理员双角色，前后端分离架构。六大模块：登录鉴权、组织架构、人事档案、考勤请假、薪酬核算、员工自助。

## 常用命令

### 后端

```bash
# 构建并运行（跳过测试）
./mvnw spring-boot:run -DskipTests

# 只构建
./mvnw compile

# 运行所有测试
./mvnw test

# 运行单个测试类
./mvnw test -Dtest=AuthServiceTest

# 打包
./mvnw package -DskipTests
```

### 前端

```bash
cd frontend
npm run dev       # 启动开发服务器 (port 5173)
npm run build     # 生产构建
npm run preview   # 预览生产构建
```

### 数据库

- MySQL 8.x, 数据库名 `employee_personnel`
- 初始化脚本: `src/main/resources/sql/init.sql`
- 启动时 `DataInitializer` 自动创建 admin/123456 管理员账户

## 后端架构

### 分层结构（Controller → Service → Mapper）

```
controller/       — REST Controllers, @RequiredArgsConstructor 构造注入
service/          — 接口定义
service/impl/     — 实现类，标注 @Service
mapper/           — MyBatis-Plus BaseMapper 接口（无 XML 映射文件）
entity/           — 实体类，继承 BaseEntity（携带 id/createTime/updateTime）
dto/              — 请求/响应 DTO
```

### 请求链路（以登录为例）

1. `AuthController` 接收 `LoginRequest` → 调用 `AuthService.login()`
2. `AuthServiceImpl` 查 `SysUserMapper` 验证密码 → 调用 `JwtTokenProvider.generateToken()`
3. 返回 `Result<LoginResponse>` 统一信封（code/message/data）

### 统一响应格式

所有 API 返回 `Result<T>` 包装：`{ code: 200, message: "success", data: ... }`。前端 Axios 拦截器自动解包 `body.data`，code !== 200 时报错。

### 安全机制

- **SecurityConfig**: 无状态 Session，禁用 CSRF，`/api/auth/**` 公开，其余需认证
- **JwtAuthenticationFilter**: OncePerRequestFilter，从 `Authorization: Bearer <token>` 提取 JWT 并设置 SecurityContext
- **JwtTokenProvider**: jjwt 0.12.6，HMAC 签名，token 含 userId/username/role，过期 24h
- 角色命名: `ROLE_ADMIN` / `ROLE_EMPLOYEE`（自动加 `ROLE_` 前缀）
- 方法级授权: `@EnableMethodSecurity` 已启用，可用 `@PreAuthorize`

### MyBatis-Plus 配置

- `log-impl: StdOutImpl`（开发时打印 SQL）
- 逻辑删除: `deleted` 字段（1=删除, 0=未删除）
- 自动填充: `MyMetaObjectHandler` 自动填充 createTime/updateTime
- MybatisPlusInterceptor 已注册但未添加分页插件（如需分页需添加 PaginationInnerInterceptor）

### 数据库表（13 张）

`sys_user` / `employee` / `department` / `position` / `reporting_relationship` / `attendance` / `leave_request` / `overtime_request` / `makeup_request` / `salary_structure` / `employee_salary` / `salary_record` / `announcement`

### 启动初始化

`DataInitializer` 实现 `CommandLineRunner`：自动创建管理员账户（admin/123456），修复残留的错误密码哈希。

## 前端架构

```
frontend/src/
├── layouts/         — AdminLayout.vue / EmployeeLayout.vue（按角色分离布局）
├── views/
│   ├── login/       — LoginView.vue
│   ├── admin/       — 管理后台页面（Dashboard, Employees, Departments, Positions, 
│   │                   Organization, Attendance, LeaveApproval, OvertimeApproval,
│   │                   MakeupApproval, SalaryStructures, EmployeeSalaries,
│   │                   SalaryCalculate, SalaryRecords）
│   └── employee/    — 员工门户页面（Dashboard, Profile, Attendance, Leave,
│                       Overtime, Makeup, Salary）
├── router/index.js  — 路由配置 + 角色守卫
├── stores/user.js   — Pinia 用户状态（token/role/employeeId 持久化到 localStorage）
├── utils/request.js — Axios 实例，jwt 拦截器 + 响应解包 + 401 自动跳转登录
└── App.vue
```

### 路由守卫逻辑

- `/login`: 已登录用户自动跳转到对应角色首页
- `/employee/*`: 需 token + role=EMPLOYEE
- `/admin/*`: 需 token + role=ADMIN
- 未匹配路由重定向到 `/login`

### Vite 配置

- 开发服务器端口 5173
- `/api` 代理到 `http://localhost:8080`
- `@` 别名指向 `src/`

## 技术栈版本

| 技术 | 版本 |
|------|------|
| Spring Boot | 3.5.14 |
| Java | 17 |
| MyBatis-Plus | 3.5.9 |
| jjwt | 0.12.6 |
| Hutool | 5.8.29 |
| Apache POI | 5.2.5 |
| Aliyun OSS SDK | 3.17.4 |
| Vue | 3.5.34 |
| Element Plus | 2.14.1 |
| Vite | 8.0.12 |
| Pinia | 3.0.4 |
| ECharts | 6.1.0 |

## Docker 部署

部署文件在 `deploy/` 目录下。

### 打包步骤

```bash
# 1. 构建后端 JAR
./mvnw package -DskipTests
cp target/*.jar deploy/backend/app.jar

# 2. 构建前端
cd frontend && npm run build
cp -r dist ../deploy/frontend/dist

# 3. 复制环境配置（可选）
cp deploy/.env.example deploy/.env
# 编辑 deploy/.env 修改密码和密钥
```

最终 deploy/ 目录结构：
```
deploy/
├── docker-compose.yml
├── .env
├── init.sql
├── backend/
│   ├── Dockerfile
│   └── app.jar
└── frontend/
    ├── Dockerfile
    ├── nginx.conf
    └── dist/
```

### 服务器部署

```bash
# 将 deploy/ 目录上传到服务器后
cd deploy
docker compose up -d
```

服务端口：前端 80，后端 8080，MySQL 3307（映射到主机）
