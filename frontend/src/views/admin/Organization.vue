<template>
  <div class="admin-org">
    <PageHeader title="组织架构图" icon="OfficeBuilding" description="公司部门层级结构总览" />
    <GlassCard padding="lg">
      <div ref="chartRef" class="org-chart"></div>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'
const chartRef = ref(null)
let chart = null
onMounted(async () => {
  const list = await request.get('/departments/organization-tree')
  chart = echarts.init(chartRef.value)
  chart.setOption({
    backgroundColor: 'transparent',
    tooltip: { trigger: 'item', triggerOn: 'mousemove' },
    series: [{
      type: 'tree',
      data: [{ name: '总公司', type: 'root', children: list }],
      orient: 'TB',
      expandAndCollapse: true,
      initialTreeDepth: 2,
      top: '3%',
      left: '5%',
      bottom: '3%',
      right: '5%',
      symbolSize: 12,
      roam: true,
      itemStyle: { color: '#3B82F6', borderColor: 'rgba(255,255,255,0.3)', borderWidth: 2 },
      lineStyle: { color: 'rgba(255,255,255,0.2)', width: 2, curveness: 0.5 },
      label: {
        position: 'top',
        verticalAlign: 'middle',
        align: 'center',
        color: '#e0e0e0',
        fontSize: 12,
        offset: [0, -8]
      },
      leaves: {
        label: {
          position: 'top',
          verticalAlign: 'middle',
          align: 'center',
          color: '#e0e0e0',
          fontSize: 12,
          offset: [0, -8]
        }
      }
    }]
  })
})
function onResize() { chart && chart.resize() }
onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))
</script>

<style scoped>
.admin-org {
  animation: float-up 0.5s ease both;
}
.org-chart {
  width: 100%;
  height: 650px;
}
</style>
