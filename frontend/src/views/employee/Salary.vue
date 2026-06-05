<template>
  <div class="emp-salary">
    <PageHeader title="我的工资条" icon="Money" />

    <GlassCard padding="lg">
      <div class="salary-picker">
        <el-input-number v-model="year" :min="2020" :max="2030" controls-position="right" />
        <span class="salary-sep">年</span>
        <el-input-number v-model="month" :min="1" :max="12" controls-position="right" />
        <span class="salary-sep">月</span>
        <el-button type="primary" @click="load">查询</el-button>
      </div>
    </GlassCard>

    <GlassCard padding="lg" class="mt-16">
      <el-table :data="records" stripe>
        <el-table-column prop="year" label="年份" />
        <el-table-column prop="month" label="月份" />
        <el-table-column prop="basicSalary" label="基本工资" />
        <el-table-column prop="performanceSalary" label="绩效工资" />
        <el-table-column prop="subsidy" label="津贴补贴" />
        <el-table-column prop="attendanceDeduction" label="考勤扣款" />
        <el-table-column prop="tax" label="个税" />
        <el-table-column prop="netSalary" label="实发工资" />
        <el-table-column label="操作">
          <template #default="{row}">
            <el-button size="small" type="primary" @click="showDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </GlassCard>

    <el-dialog v-model="dialogVisible" title="工资条详情" width="520px">
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="基本工资">{{ detail.basicSalary }}</el-descriptions-item>
        <el-descriptions-item label="绩效工资">{{ detail.performanceSalary }}</el-descriptions-item>
        <el-descriptions-item label="津贴补贴">{{ detail.subsidy }}</el-descriptions-item>
        <el-descriptions-item label="考勤扣款">{{ detail.attendanceDeduction }}</el-descriptions-item>
        <el-descriptions-item label="社保个人">{{ detail.socialInsurancePersonal }}</el-descriptions-item>
        <el-descriptions-item label="公积金个人">{{ detail.housingFundPersonal }}</el-descriptions-item>
        <el-descriptions-item label="应纳税所得额">{{ detail.taxableIncome }}</el-descriptions-item>
        <el-descriptions-item label="个税">{{ detail.tax }}</el-descriptions-item>
        <el-descriptions-item label="实发工资" :span="2">
          <b style="font-size:18px;color:var(--color-primary)">{{ detail.netSalary }}</b>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
const store = useUserStore()
const headers = { 'X-Employee-Id': store.employeeId }
const year = ref(new Date().getFullYear())
const month = ref(new Date().getMonth() + 1)
const records = ref([])
const dialogVisible = ref(false)
const detail = ref(null)
async function load() { const r = await request.get('/salary/records/me', { params: { year: year.value, month: month.value }, headers }); records.value = r; }
async function showDetail(row) { const r = await request.get(`/salary/records/${row.id}`); detail.value = r; dialogVisible.value = true; }
</script>

<style scoped>
.emp-salary {
  animation: float-up 0.5s ease both;
}
.salary-picker {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}
.salary-sep {
  color: var(--text-secondary);
}
.mt-16 {
  margin-top: 16px;
}
</style>
