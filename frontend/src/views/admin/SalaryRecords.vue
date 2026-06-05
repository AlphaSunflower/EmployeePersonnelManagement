<template>
  <div class="admin-sr">
    <PageHeader title="工资记录" icon="Money" />
    <GlassCard padding="lg">
      <div class="record-filter">
        <el-input-number v-model="filter.year" :min="2020" controls-position="right" />
        <span class="record-sep">年</span>
        <el-input-number v-model="filter.month" :min="1" :max="12" controls-position="right" />
        <span class="record-sep">月</span>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="exportRecords">导出Excel</el-button>
      </div>
    </GlassCard>
    <GlassCard padding="lg" class="mt-16">
      <el-table :data="records" stripe>
        <el-table-column prop="employeeId" label="员工ID" />
        <el-table-column prop="year" label="年份" />
        <el-table-column prop="month" label="月份" />
        <el-table-column prop="basicSalary" label="基本工资" />
        <el-table-column prop="netSalary" label="实发工资" />
      </el-table>
      <template #footer>
        <el-pagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" @current-change="load" />
      </template>
    </GlassCard>

    <!-- Salary trend chart -->
    <GlassCard title="月度薪酬趋势" padding="md" class="mt-16">
      <div ref="chartRef" class="salary-chart"></div>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'
import { downloadFile } from '@/utils/download'
import { ElMessage } from 'element-plus'
const records = ref([]);const filter = reactive({ year: new Date().getFullYear(), month: new Date().getMonth() + 1 });const page = reactive({ current: 1, size: 10, total: 0 })
const chartRef = ref(null)
let chart = null
onMounted(() => { load(); loadChart() })
async function load() { const r = await request.get('/salary/records', { params: { current: page.current, size: page.size, year: filter.year, month: filter.month } });records.value = r.records;page.total = r.total }
async function exportRecords() {
  try {
    await downloadFile('/salary/records/export', { year: filter.year, month: filter.month }, `薪酬记录_${filter.year}年${filter.month}月.xlsx`)
    ElMessage.success('导出成功')
  } catch (e) { ElMessage.error('导出失败') }
}
async function loadChart() {
  try {
    const y = filter.year
    const months = []; const values = []; const totals = []
    for (let m = 1; m <= 12; m++) {
      months.push(`${m}月`)
      try {
        const r = await request.get('/salary/records', { params: { year: y, month: m, size: 1 } })
        totals.push(r.total || 0)
      } catch (e) { totals.push(0) }
    }
    chart = echarts.init(chartRef.value)
    chart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '3%', bottom: '3%', top: '5%', containLabel: true },
      xAxis: { type: 'category', data: months, axisLabel: { color: '#aaa', fontSize: 11 }, axisLine: { lineStyle: { color: 'rgba(255,255,255,0.15)' } } },
      yAxis: { type: 'value', name: '人数', nameTextStyle: { color: '#aaa' }, axisLabel: { color: '#aaa' }, splitLine: { lineStyle: { color: 'rgba(255,255,255,0.06)' } } },
      series: [{
        type: 'bar',
        data: totals,
        barWidth: 20,
        itemStyle: { color: '#3B82F6', borderRadius: [4, 4, 0, 0] },
        emphasis: { itemStyle: { color: '#60A5FA' } }
      }]
    })
  } catch (e) {}
}
function onResize() { chart && chart.resize() }
onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))
</script>

<style scoped>
.admin-sr { animation: float-up 0.5s ease both; }
.record-filter { display: flex; align-items: center; gap: var(--space-2); }
.record-sep { color: var(--text-secondary); }
.mt-16 { margin-top: 16px; }
.salary-chart { width: 100%; height: 300px; }
</style>
