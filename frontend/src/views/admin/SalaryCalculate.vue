<template>
  <div class="admin-sc">
    <PageHeader title="月度薪酬核算" icon="Money" description="一键核算指定月份的所有员工工资" />
    <GlassCard padding="lg">
      <div class="calc-form">
        <el-input-number v-model="year" :min="2020" controls-position="right" size="large" />
        <span class="calc-sep">年</span>
        <el-input-number v-model="month" :min="1" :max="12" controls-position="right" size="large" />
        <span class="calc-sep">月</span>
        <el-button type="primary" size="large" :loading="loading" @click="calculate" class="calc-btn">开始核算</el-button>
      </div>
      <div v-if="done" class="calc-done">
        <el-icon class="calc-done__icon"><component :is="'CircleCheckFilled'" /></el-icon>
        <span>{{ year }}年{{ month }}月薪酬核算完成</span>
      </div>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref } from 'vue';import request from '@/utils/request';import { ElMessage } from 'element-plus'
const year = ref(new Date().getFullYear());const month = ref(new Date().getMonth() + 1);const loading = ref(false);const done = ref(false)
async function calculate() { loading.value = true;try { await request.post('/salary/calculate', null, { params: { year: year.value, month: month.value } });done.value = true;ElMessage.success('核算完成') } finally { loading.value = false } }
</script>

<style scoped>
.admin-sc {
  animation: float-up 0.5s ease both;
}
.calc-form {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.calc-sep {
  color: var(--text-secondary);
  font-size: var(--font-size-lg);
}
.calc-btn {
  margin-left: var(--space-4);
}
.calc-done {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  margin-top: var(--space-6);
  padding: var(--space-4);
  background: hsla(160, 84%, 39%, 0.1);
  border: 1px solid hsla(160, 84%, 39%, 0.25);
  border-radius: var(--radius-sm);
  color: var(--color-success);
  font-size: var(--font-size-lg);
  font-weight: 500;
}
.calc-done__icon {
  font-size: var(--font-size-xl);
}
</style>
