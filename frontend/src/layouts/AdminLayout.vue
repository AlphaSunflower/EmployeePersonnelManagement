<template>
  <el-container class="layout">
    <el-aside width="240px" class="glass-sidebar">
      <div class="sidebar-logo">
        <div class="sidebar-logo__icon">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="3" width="18" height="18" rx="3" />
            <path d="M9 12h6M12 9v6" />
          </svg>
        </div>
        <span class="sidebar-logo__text">管理后台</span>
      </div>
      <div class="sidebar-menu-wrap">
        <el-menu :default-active="$route.path" router>
          <el-menu-item index="/admin/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>工作台</span>
          </el-menu-item>
          <el-sub-menu index="org">
            <template #title>
              <el-icon><OfficeBuilding /></el-icon>
              <span>组织架构</span>
            </template>
            <el-menu-item index="/admin/departments">部门管理</el-menu-item>
            <el-menu-item index="/admin/positions">岗位管理</el-menu-item>
            <el-menu-item index="/admin/organization">组织架构图</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/admin/employees">
            <el-icon><UserFilled /></el-icon>
            <span>员工管理</span>
          </el-menu-item>
          <el-sub-menu index="att">
            <template #title>
              <el-icon><Clock /></el-icon>
              <span>考勤管理</span>
            </template>
            <el-menu-item index="/admin/attendance">考勤汇总</el-menu-item>
            <el-menu-item index="/admin/leave-approval">请假审批</el-menu-item>
            <el-menu-item index="/admin/overtime-approval">加班审批</el-menu-item>
            <el-menu-item index="/admin/makeup-approval">补签审批</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="sal">
            <template #title>
              <el-icon><Money /></el-icon>
              <span>薪酬管理</span>
            </template>
            <el-menu-item index="/admin/salary-structures">薪酬结构</el-menu-item>
            <el-menu-item index="/admin/employee-salaries">员工薪酬</el-menu-item>
            <el-menu-item index="/admin/salary-calculate">薪酬核算</el-menu-item>
            <el-menu-item index="/admin/salary-records">工资记录</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <div class="sidebar-footer">
        <span class="sidebar-footer__ver">v1.0</span>
      </div>
    </el-aside>
    <el-container>
      <el-header class="glass-header" height="56px">
        <div class="header-left">
          <span class="header-greeting">欢迎，管理员 <strong>{{ store.username }}</strong></span>
        </div>
        <div class="header-right">
          <el-button class="logout-btn" @click="handleLogout" size="small">
            <el-icon><component :is="'SwitchButton'" /></el-icon>
            退出登录
          </el-button>
        </div>
      </el-header>
      <el-main class="glass-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
const store = useUserStore()
const router = useRouter()
function handleLogout() { store.logout(); router.push('/login') }
</script>

<style scoped>
.layout {
  height: 100vh;
  position: relative;
  z-index: 1;
}

/* === Sidebar === */
.glass-sidebar {
  background: var(--bg-sidebar);
  backdrop-filter: blur(var(--glass-blur-lg));
  -webkit-backdrop-filter: blur(var(--glass-blur-lg));
  border-right: 1px solid var(--border-glass-medium);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-5) var(--space-6);
  border-bottom: 1px solid var(--border-glass-light);
}

.sidebar-logo__icon {
  color: var(--color-primary);
  display: flex;
  align-items: center;
}

.sidebar-logo__text {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: -0.2px;
}

.sidebar-menu-wrap {
  flex: 1;
  overflow-y: auto;
  padding: var(--space-2) 0;
  scroll-behavior: auto;
}

.sidebar-footer {
  padding: var(--space-3) var(--space-6);
  border-top: 1px solid var(--border-glass-light);
}

.sidebar-footer__ver {
  font-size: var(--font-size-xs);
  color: var(--text-muted);
}

/* === Header === */
.glass-header {
  background: var(--bg-header);
  backdrop-filter: blur(var(--glass-blur-md));
  -webkit-backdrop-filter: blur(var(--glass-blur-md));
  border-bottom: 1px solid var(--border-glass-medium);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 var(--space-6);
  height: 56px;
}

.header-greeting {
  font-size: var(--font-size-body);
  color: var(--text-secondary);
}

.header-greeting strong {
  color: var(--text-primary);
  font-weight: 600;
}

.logout-btn {
  background: hsla(0, 84%, 60%, 0.12);
  border: 1px solid hsla(0, 84%, 60%, 0.25);
  color: hsla(0, 84%, 60%, 1);
  font-weight: 500;
}

.logout-btn:hover {
  background: hsla(0, 84%, 60%, 0.2);
  border-color: hsla(0, 84%, 60%, 0.4);
}

/* === Main content === */
.glass-main {
  padding: var(--space-6);
  overflow-y: auto;
  min-height: 0;
}
</style>
