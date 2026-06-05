<template>
  <div class="admin-mu">
    <PageHeader title="补签审批" icon="EditPen" />
    <GlassCard padding="lg">
      <el-table :data="records" stripe>
        <el-table-column prop="employeeId" label="员工ID" />
        <el-table-column prop="date" label="日期" />
        <el-table-column label="类型">
          <template #default="{row}">{{ row.type==='CHECK_IN'?'上班卡':'下班卡' }}</template>
        </el-table-column>
        <el-table-column prop="reason" label="原因" />
        <el-table-column label="操作" width="200">
          <template #default="{row}">
            <el-button size="small" type="success" @click="approve(row.id,'APPROVED')">通过</el-button>
            <el-button size="small" type="danger" @click="approve(row.id,'REJECTED')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';import request from '@/utils/request';import { ElMessage } from 'element-plus'
const records = ref([])
onMounted(async () => { const r = await request.get('/makeup-requests/pending');records.value = r.records })
async function approve(id, status) { await request.put(`/makeup-requests/${id}/approve`, { status, approverId: '1', comment: '' });ElMessage.success(status === 'APPROVED' ? '已通过' : '已驳回');const r = await request.get('/makeup-requests/pending');records.value = r.records }
</script>

<style scoped>
.admin-mu {
  animation: float-up 0.5s ease both;
}
</style>
