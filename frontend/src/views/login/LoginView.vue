<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-brand">
        <div class="login-brand__logo">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2" />
            <circle cx="9" cy="7" r="4" />
            <path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75" />
          </svg>
        </div>
        <h1 class="login-brand__title">员工人事管理系统</h1>
        <p class="login-brand__subtitle">Employee Personnel Management</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" class="login-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" size="large" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" size="large" />
        </el-form-item>
        <el-button type="primary" :loading="loading" @click="handleLogin" class="login-btn" size="large">
          {{ loading ? '登录中...' : '登 录' }}
        </el-button>
      </el-form>
      <p class="login-hint">管理员: admin / 123456 &nbsp;|&nbsp; 员工: 手机号 / 123456</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const store = useUserStore()
const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await store.login(form)
    ElMessage.success('登录成功')
    router.push(store.role === 'ADMIN' ? '/admin' : '/employee')
  } catch (e) {
    // Error message already shown by request interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  z-index: 1;
}

.login-card {
  width: 420px;
  padding: var(--space-10);
  background: var(--bg-card);
  backdrop-filter: blur(var(--glass-blur-lg));
  -webkit-backdrop-filter: blur(var(--glass-blur-lg));
  border: 1px solid var(--border-glass-strong);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl), var(--shadow-glow), inset 0 1px 0 hsla(255, 255, 255, 0.06);
}

.login-brand {
  text-align: center;
  margin-bottom: var(--space-8);
}

.login-brand__logo {
  color: var(--color-primary);
  display: flex;
  justify-content: center;
  margin-bottom: var(--space-4);
}

.login-brand__title {
  margin: 0;
  font-size: var(--font-size-xl);
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: -0.3px;
}

.login-brand__subtitle {
  margin: var(--space-1) 0 0;
  font-size: var(--font-size-xs);
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 2px;
}

.login-form {
  margin-top: var(--space-2);
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: var(--font-size-lg);
  font-weight: 600;
  letter-spacing: 2px;
  margin-top: var(--space-2);
}

.login-hint {
  text-align: center;
  color: var(--text-muted);
  font-size: var(--font-size-xs);
  margin: var(--space-6) 0 0;
  opacity: 0.6;
}
</style>
