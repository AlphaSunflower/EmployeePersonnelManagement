<template>
  <div class="emp-attendance">
    <PageHeader title="考勤打卡" icon="Clock" />

    <el-row :gutter="20">
      <el-col :span="8">
        <GlassCard title="上班打卡" padding="md">
          <el-button type="primary" size="large" @click="checkIn" :disabled="today.checkedIn" class="att-btn">
            打卡签到
          </el-button>
          <p v-if="today.checkIn" class="att-time">打卡时间: {{ today.checkIn }}</p>
        </GlassCard>
      </el-col>
      <el-col :span="8">
        <GlassCard title="下班打卡" padding="md">
          <el-button type="success" size="large" @click="checkOut" :disabled="!today.checkedIn || today.checkedOut" class="att-btn">
            打卡签退
          </el-button>
          <p v-if="today.checkOut" class="att-time">打卡时间: {{ today.checkOut }}</p>
        </GlassCard>
      </el-col>
      <el-col :span="8">
        <GlassCard title="今日状态" padding="md">
          <el-tag :type="today.status === 'NORMAL' ? 'success' : 'warning'" size="large" class="att-tag">
            {{ statusLabel }}
          </el-tag>
        </GlassCard>
      </el-col>
    </el-row>

    <GlassCard title="月度考勤汇总" padding="lg" style="margin-top:16px">
      <div class="month-picker">
        <el-input-number v-model="year" :min="2020" :max="2030" controls-position="right" />
        <span class="month-sep">年</span>
        <el-input-number v-model="month" :min="1" :max="12" controls-position="right" />
        <span class="month-sep">月</span>
        <el-button type="primary" @click="loadMonthly">查询</el-button>
      </div>
      <el-descriptions v-if="monthly" :column="5" border style="margin-top:16px">
        <el-descriptions-item label="出勤天数">{{ monthly.total }}</el-descriptions-item>
        <el-descriptions-item label="正常">{{ monthly.normal }}</el-descriptions-item>
        <el-descriptions-item label="迟到">{{ monthly.late }}</el-descriptions-item>
        <el-descriptions-item label="早退">{{ monthly.early }}</el-descriptions-item>
        <el-descriptions-item label="缺勤">{{ monthly.absent }}</el-descriptions-item>
      </el-descriptions>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
const store = useUserStore()
const headers = { 'X-Employee-Id': store.employeeId }
const today = reactive({ checkedIn: false, checkedOut: false, checkIn: null, checkOut: null, status: '-' })
const year = ref(new Date().getFullYear())
const month = ref(new Date().getMonth() + 1)
const monthly = ref(null)
const statusLabel = computed(() => {
  const map = { NORMAL: '正常', LATE: '迟到', EARLY: '早退' }
  return map[today.status] || today.status || '-'
})
onMounted(async () => { try { const res = await request.get('/attendance/today', { headers }); Object.assign(today, res) } catch (e) {} })
async function checkIn() { const r = await request.post('/attendance/check-in', null, { headers }); Object.assign(today, r); today.checkedIn = true; ElMessage.success('打卡成功') }
async function checkOut() { const r = await request.post('/attendance/check-out', null, { headers }); Object.assign(today, r); today.checkedOut = true; ElMessage.success('打卡成功') }
async function loadMonthly() { const r = await request.get('/attendance/monthly', { params: { year: year.value, month: month.value }, headers }); monthly.value = r }
</script>

<style scoped>
.emp-attendance {
  animation: float-up 0.5s ease both;
}
.att-btn {
  width: 100%;
  height: 48px;
  font-size: var(--font-size-lg);
}
.att-time {
  margin-top: var(--space-3);
  font-size: var(--font-size-sm);
  color: var(--text-muted);
}
.att-tag {
  font-size: var(--font-size-lg);
  padding: var(--space-2) var(--space-6);
}
.month-picker {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.month-sep {
  color: var(--text-secondary);
  font-size: var(--font-size-body);
}
</style>
