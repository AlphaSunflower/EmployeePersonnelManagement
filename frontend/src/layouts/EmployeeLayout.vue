<template>
  <el-container class="layout">
    <el-aside width="220px" class="glass-sidebar">
      <div class="sidebar-logo">
        <div class="sidebar-logo__icon">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="8" r="4" />
            <path d="M4 20c0-4 4-7 8-7s8 3 8 7" />
          </svg>
        </div>
        <span class="sidebar-logo__text">员工门户</span>
      </div>
      <div class="sidebar-menu-wrap">
        <el-menu :default-active="$route.path" router>
          <el-menu-item index="/employee/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>工作台</span>
          </el-menu-item>
          <el-menu-item index="/employee/profile">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="/employee/attendance">
            <el-icon><Clock /></el-icon>
            <span>考勤打卡</span>
          </el-menu-item>
          <el-menu-item index="/employee/leave">
            <el-icon><Document /></el-icon>
            <span>请假申请</span>
          </el-menu-item>
          <el-menu-item index="/employee/overtime">
            <el-icon><Timer /></el-icon>
            <span>加班申请</span>
          </el-menu-item>
          <el-menu-item index="/employee/makeup">
            <el-icon><EditPen /></el-icon>
            <span>补签申请</span>
          </el-menu-item>
          <el-menu-item index="/employee/salary">
            <el-icon><Money /></el-icon>
            <span>工资条</span>
          </el-menu-item>
        </el-menu>
      </div>
      <div class="sidebar-footer">
        <span class="sidebar-footer__ver">v1.0</span>
      </div>
    </el-aside>
    <el-container>
      <el-header class="glass-header" height="56px">
        <div class="header-left">
          <span class="header-greeting">欢迎，<strong>{{ store.username }}</strong></span>
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
