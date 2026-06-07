<template>
  <div class="emp-dashboard">
    <PageHeader title="工作台" :description="`欢迎回来，${store.username}`" icon="HomeFilled" />
    <el-row :gutter="20">
      <el-col :span="8">
        <GlassCard class="dash-card" title="今日日期" padding="md">
          <p class="dash-card__big">{{ todayStr }}</p>
          <p class="dash-card__label">{{ dayOfWeek }}</p>
        </GlassCard>
      </el-col>
      <el-col :span="8">
        <GlassCard class="dash-card" title="快捷操作" padding="md">
          <el-button type="primary" size="large" @click="$router.push('/employee/attendance')" class="dash-btn">
            <el-icon><Clock /></el-icon> 去打卡
          </el-button>
        </GlassCard>
      </el-col>
      <el-col :span="8">
        <GlassCard class="dash-card" title="我的信息" padding="md">
          <div class="info-card">
            <el-avatar :src="profile?.avatarUrl" :size="48">{{ store.username?.charAt(0) }}</el-avatar>
            <div>
              <p class="dash-card__big">{{ profile?.name || store.username }}</p>
              <p class="dash-card__label">{{ profile?.deptName || '员工门户' }}</p>
            </div>
          </div>
        </GlassCard>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <GlassCard title="本月考勤统计" padding="md">
          <div ref="attChartRef" class="dash-chart"></div>
        </GlassCard>
      </el-col>
      <el-col :span="12">
        <GlassCard title="近6个月薪资趋势" padding="md">
          <div ref="salaryChartRef" class="dash-chart"></div>
        </GlassCard>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
const store = useUserStore()
const now = new Date()
const todayStr = now.toLocaleDateString()
const days = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六']
const dayOfWeek = days[now.getDay()]
const attChartRef = ref(null)
const salaryChartRef = ref(null)
let attChart = null
let salaryChart = null
const profile = ref(null)
const headers = { 'X-Employee-Id': store.employeeId, 'X-User-Id': store.userId }

onMounted(async () => {
  try { profile.value = await request.get('/employees/me', { headers }) } catch (e) {}
  // Monthly attendance
  try {
    const m = await request.get('/attendance/monthly', {
      params: { year: now.getFullYear(), month: now.getMonth() + 1 },
      headers
    })
    attChart = echarts.init(attChartRef.value)
    attChart.setOption({
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
          { value: m.normal || 0, name: '正常', itemStyle: { color: '#10B981' } },
          { value: m.late || 0, name: '迟到', itemStyle: { color: '#F59E0B' } },
          { value: m.early || 0, name: '早退', itemStyle: { color: '#F97316' } },
          { value: m.absent || 0, name: '缺勤', itemStyle: { color: '#EF4444' } }
        ]
      }]
    })
  } catch (e) {}

  // Salary trend (last 6 months)
  try {
    const months = []
    const values = []
    const y = now.getFullYear()
    const cm = now.getMonth() + 1
    for (let i = 5; i >= 0; i--) {
      let m = cm - i; let yr = y
      if (m <= 0) { m += 12; yr-- }
      months.push(`${yr}/${m}`)
      try {
        const r = await request.get('/salary/records/me', { params: { year: yr, month: m }, headers })
        values.push(r.length > 0 ? (r[0].netSalary || 0) : 0)
      } catch (e) { values.push(0) }
    }
    salaryChart = echarts.init(salaryChartRef.value)
    salaryChart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '3%', bottom: '3%', top: '8%', containLabel: true },
      xAxis: { type: 'category', data: months, axisLabel: { color: '#aaa', fontSize: 11 }, axisLine: { lineStyle: { color: 'rgba(255,255,255,0.15)' } } },
      yAxis: { type: 'value', axisLabel: { color: '#aaa' }, splitLine: { lineStyle: { color: 'rgba(255,255,255,0.06)' } } },
      series: [{
        type: 'line',
        data: values,
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { color: '#3B82F6', width: 2 },
        itemStyle: { color: '#3B82F6' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(59,130,246,0.3)' },
          { offset: 1, color: 'rgba(59,130,246,0.02)' }
        ]) }
      }]
    })
  } catch (e) {}
})

function onResize() { attChart && attChart.resize(); salaryChart && salaryChart.resize() }
onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))
</script>

<style scoped>
.emp-dashboard { animation: float-up 0.5s ease both; }
.dash-card { height: 100%; }
.dash-card__big { font-size: var(--font-size-2xl); font-weight: 700; color: var(--text-primary); margin: var(--space-2) 0; }
.dash-card__label { font-size: var(--font-size-sm); color: var(--text-muted); margin: 0; }
.dash-btn { width: 100%; height: 48px; font-size: var(--font-size-lg); margin-top: var(--space-3); }
.dash-chart { width: 100%; height: 280px; }
.info-card { display: flex; align-items: center; gap: var(--space-4); }
.info-card .dash-card__big { margin: 0; }
</style>
