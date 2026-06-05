<template>
  <div class="admin-leave">
    <PageHeader title="请假审批" icon="Document" />
    <GlassCard padding="lg">
      <el-table :data="records" stripe>
        <el-table-column prop="employeeId" label="员工ID" />
        <el-table-column label="类型">
          <template #default="{row}">{{ row.type==='ANNUAL'?'年假':row.type==='SICK'?'病假':'事假' }}</template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始" />
        <el-table-column prop="endDate" label="结束" />
        <el-table-column prop="days" label="天数" />
        <el-table-column prop="reason" label="原因" />
        <el-table-column label="操作" width="200">
          <template #default="{row}">
            <el-button size="small" type="success" @click="approve(row.id,'APPROVED')">通过</el-button>
            <el-button size="small" type="danger" @click="approve(row.id,'REJECTED')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-pagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" @current-change="load" />
      </template>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';import request from '@/utils/request';import { ElMessage } from 'element-plus'
const records = ref([]);const page = reactive({ current: 1, size: 10, total: 0 })
onMounted(load)
async function load() { const r = await request.get('/leave-requests/pending', { params: { current: page.current, size: page.size } });records.value = r.records;page.total = r.total }
async function approve(id, status) { await request.put(`/leave-requests/${id}/approve`, { status, approverId: '1', comment: '' });ElMessage.success(status === 'APPROVED' ? '已通过' : '已驳回');load() }
</script>

<style scoped>
.admin-leave {
  animation: float-up 0.5s ease both;
}
</style>
