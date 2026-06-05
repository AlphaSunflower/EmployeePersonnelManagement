<template>
  <div class="admin-dash">
    <PageHeader title="管理后台" description="系统运行概览" icon="HomeFilled" />
    <el-row :gutter="20">
      <el-col :span="6">
        <GlassCard class="stat-card" padding="md">
          <div class="stat-card__icon stat-card__icon--blue"><el-icon><UserFilled /></el-icon></div>
          <p class="stat-card__value">{{ stats.totalEmployees }}</p>
          <p class="stat-card__label">员工总数</p>
        </GlassCard>
      </el-col>
      <el-col :span="6">
        <GlassCard class="stat-card" padding="md">
          <div class="stat-card__icon stat-card__icon--green"><el-icon><UserFilled /></el-icon></div>
          <p class="stat-card__value">{{ stats.active }}</p>
          <p class="stat-card__label">在职员工</p>
        </GlassCard>
      </el-col>
      <el-col :span="6">
        <GlassCard class="stat-card" padding="md">
          <div class="stat-card__icon stat-card__icon--cyan"><el-icon><OfficeBuilding /></el-icon></div>
          <p class="stat-card__value">{{ stats.departments }}</p>
          <p class="stat-card__label">部门数</p>
        </GlassCard>
      </el-col>
      <el-col :span="6">
        <GlassCard class="stat-card" padding="md">
          <div class="stat-card__icon stat-card__icon--warning"><el-icon><Clock /></el-icon></div>
          <p class="stat-card__value">{{ stats.pending }}</p>
          <p class="stat-card__label">待审批</p>
        </GlassCard>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <GlassCard title="员工状态分布" padding="md">
          <div ref="statusChartRef" class="dash-chart"></div>
        </GlassCard>
      </el-col>
      <el-col :span="12">
        <GlassCard title="部门人数统计" padding="md">
          <div ref="deptChartRef" class="dash-chart"></div>
        </GlassCard>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'
const stats = ref({ totalEmployees: 0, active: 0, departments: 0, pending: 0 })
const statusChartRef = ref(null)
const deptChartRef = ref(null)
let statusChart = null
let deptChart = null

onMounted(async () => {
  try { const r = await request.get('/employees?size=1'); stats.value.totalEmployees = r.total } catch (e) {}
  try { const r = await request.get('/employees?status=ACTIVE&size=1'); stats.value.active = r.total } catch (e) {}
  try { const r = await request.get('/departments/list'); stats.value.departments = r.length } catch (e) {}
  try { const r = await request.get('/leave-requests/pending?size=1'); stats.value.pending = (r.total || 0) } catch (e) {}

  // Employee status distribution
  try {
    const [activeR, proR, resR] = await Promise.all([
      request.get('/employees?status=ACTIVE&size=1'),
      request.get('/employees?status=PROBATION&size=1'),
      request.get('/employees?status=RESIGNED&size=1')
    ])
    statusChart = echarts.init(statusChartRef.value)
    statusChart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'item' },
      legend: { bottom: 0, textStyle: { color: '#aaa', fontSize: 11 } },
      series: [{
        type: 'pie',
        radius: ['55%', '78%'],
        center: ['50%', '48%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 4, borderColor: 'transparent', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
        data: [
          { value: activeR.total || 0, name: '在职', itemStyle: { color: '#10B981' } },
          { value: proR.total || 0, name: '试用期', itemStyle: { color: '#F59E0B' } },
          { value: resR.total || 0, name: '离职', itemStyle: { color: '#EF4444' } }
        ]
      }]
    })
  } catch (e) {}

  // Department headcount
  try {
    const [deptList, empAll] = await Promise.all([
      request.get('/departments/list'),
      request.get('/employees?size=500&status=ACTIVE')
    ])
    const empRecords = empAll.records || []
    const names = deptList.map(d => d.name)
    const counts = deptList.map(d => empRecords.filter(e => e.deptId === d.id).length)
    deptChart = echarts.init(deptChartRef.value)
    deptChart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '8%', bottom: '3%', top: '8%', containLabel: true },
      xAxis: { type: 'value', axisLabel: { color: '#aaa' }, splitLine: { lineStyle: { color: 'rgba(255,255,255,0.06)' } } },
      yAxis: { type: 'category', data: names, axisLabel: { color: '#aaa', fontSize: 11 }, axisLine: { show: false }, axisTick: { show: false } },
      series: [{
        type: 'bar',
        data: counts,
        barWidth: 16,
        itemStyle: { color: '#3B82F6', borderRadius: [0, 4, 4, 0] },
        emphasis: { itemStyle: { color: '#60A5FA' } }
      }]
    })
  } catch (e) {}
})

function onResize() { statusChart && statusChart.resize(); deptChart && deptChart.resize() }
onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))
</script>

<style scoped>
.admin-dash { animation: float-up 0.5s ease both; }
.stat-card { text-align: center; }
.stat-card__icon { width: 48px; height: 48px; border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; margin: 0 auto var(--space-3); font-size: 20px; }
.stat-card__icon--blue { background: hsla(217, 89%, 61%, 0.15); color: var(--color-primary); }
.stat-card__icon--green { background: hsla(160, 84%, 39%, 0.15); color: var(--color-success); }
.stat-card__icon--cyan { background: hsla(190, 80%, 55%, 0.15); color: hsla(190, 80%, 55%, 1); }
.stat-card__icon--warning { background: hsla(38, 92%, 50%, 0.15); color: var(--color-warning); }
.stat-card__value { font-size: var(--font-size-3xl); font-weight: 700; color: var(--text-primary); margin: 0; line-height: 1.2; }
.stat-card__label { font-size: var(--font-size-sm); color: var(--text-muted); margin: var(--space-1) 0 0; }
.dash-chart { width: 100%; height: 280px; }
</style>
