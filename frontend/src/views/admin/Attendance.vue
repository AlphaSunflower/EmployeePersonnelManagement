<template>
  <div class="admin-att">
    <PageHeader title="考勤汇总" icon="Clock" />
    <GlassCard padding="lg">
      <div class="att-filter">
        <el-input-number v-model="year" :min="2020" controls-position="right" />
        <span class="att-sep">年</span>
        <el-input-number v-model="month" :min="1" :max="12" controls-position="right" />
        <span class="att-sep">月</span>
        <el-button type="primary" @click="load">查询</el-button>
      </div>
    </GlassCard>
    <GlassCard padding="lg" class="mt-16">
      <el-table :data="records" stripe>
        <el-table-column prop="employeeId" label="员工ID" />
        <el-table-column prop="total" label="出勤天数" />
        <el-table-column prop="NORMAL" label="正常" />
        <el-table-column prop="LATE" label="迟到" />
        <el-table-column prop="EARLY" label="早退" />
        <el-table-column prop="ABSENT" label="缺勤" />
      </el-table>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref } from 'vue';import request from '@/utils/request'
const year = ref(new Date().getFullYear());const month = ref(new Date().getMonth() + 1);const records = ref([])
async function load() { const r = await request.get('/attendance/monthly/all', { params: { year: year.value, month: month.value } });records.value = r }
</script>

<style scoped>
.admin-att {
  animation: float-up 0.5s ease both;
}
.att-filter {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.att-sep {
  color: var(--text-secondary);
}
.mt-16 {
  margin-top: 16px;
}
</style>
