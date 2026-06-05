<template>
  <div class="emp-profile">
    <PageHeader title="个人信息" icon="User" />

    <GlassCard v-if="profile" title="基本信息" padding="lg">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ profile.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ profile.gender === 1 ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ profile.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ profile.email }}</el-descriptions-item>
        <el-descriptions-item label="入职日期">{{ profile.hireDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ profile.status }}</el-descriptions-item>
      </el-descriptions>

      <div class="avatar-section">
        <el-avatar v-if="profile.avatarUrl" :src="profile.avatarUrl" :size="80" class="profile-avatar" />
        <el-upload :action="uploadUrl" :headers="headers" :on-success="onUploadSuccess" :show-file-list="false" accept="image/*">
          <el-button type="primary">上传头像</el-button>
        </el-upload>
      </div>

      <el-divider />

      <h3 class="section-title">修改联系方式</h3>
      <el-form :model="form" label-width="120px" class="profile-form">
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="紧急联系人">
          <el-input v-model="form.emergencyContact" />
        </el-form-item>
        <el-form-item label="紧急联系电话">
          <el-input v-model="form.emergencyPhone" />
        </el-form-item>
        <el-button type="primary" @click="saveProfile">保存修改</el-button>
      </el-form>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
const store = useUserStore()
const profile = ref(null)
const uploadUrl = '/api/employees/me/avatar'
const headers = { Authorization: `Bearer ${store.token}`, 'X-User-Id': store.userId }
const form = reactive({ phone: '', email: '', emergencyContact: '', emergencyPhone: '' })
onMounted(async () => { const res = await request.get('/employees/me', { headers }); profile.value = res; Object.assign(form, res) })
function onUploadSuccess(res) { profile.value.avatarUrl = res.data; ElMessage.success('头像上传成功') }
async function saveProfile() { await request.put('/employees/me', form, { headers }); ElMessage.success('修改成功') }
</script>

<style scoped>
.emp-profile {
  animation: float-up 0.5s ease both;
}
.avatar-section {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  margin-top: var(--space-6);
}
.profile-avatar {
  border: 2px solid var(--border-glass-strong);
  box-shadow: var(--shadow-md);
}
.section-title {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 var(--space-4);
}
.profile-form {
  max-width: 560px;
}
</style>
