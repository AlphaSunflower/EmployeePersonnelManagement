<template>
  <div class="emp-leave">
    <PageHeader title="请假申请" icon="Document" />

    <GlassCard title="提交请假" padding="lg">
      <el-form :model="form" label-width="100px" class="leave-form">
        <el-form-item label="请假类型">
          <el-select v-model="form.type" style="width:100%">
            <el-option label="年假" value="ANNUAL" />
            <el-option label="病假" value="SICK" />
            <el-option label="事假" value="PERSONAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="form.startDate" type="date" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="form.endDate" type="date" style="width:100%" />
        </el-form-item>
        <el-form-item label="请假原因">
          <el-input v-model="form.reason" type="textarea" :rows="3" />
        </el-form-item>
        <el-button type="primary" @click="submit">提交申请</el-button>
      </el-form>
    </GlassCard>

    <GlassCard title="我的请假记录" padding="lg" class="mt-16">
      <el-table :data="records" stripe>
        <el-table-column label="类型">
          <template #default="{row}">{{ row.type==='ANNUAL'?'年假':row.type==='SICK'?'病假':'事假' }}</template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" />
        <el-table-column prop="endDate" label="结束日期" />
        <el-table-column prop="days" label="天数" />
        <el-table-column label="状态">
          <template #default="{row}">
            <el-tag :type="row.status==='APPROVED'?'success':row.status==='REJECTED'?'danger':'warning'">
              {{ row.status==='APPROVED'?'已通过':row.status==='REJECTED'?'已驳回':'待审批' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
const store = useUserStore()
const headers = { 'X-Employee-Id': store.employeeId }
const form = reactive({ type: 'ANNUAL', startDate: '', endDate: '', reason: '' })
const records = ref([])
onMounted(async () => { const r = await request.get('/leave-requests', { headers }); records.value = r.records })
async function submit() { await request.post('/leave-requests', form, { headers }); ElMessage.success('提交成功'); const r = await request.get('/leave-requests', { headers }); records.value = r.records }
</script>

<style scoped>
.emp-leave {
  animation: float-up 0.5s ease both;
}
.leave-form {
  max-width: 500px;
}
.mt-16 {
  margin-top: 16px;
}
</style>
