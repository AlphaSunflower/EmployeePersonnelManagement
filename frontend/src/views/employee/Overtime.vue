<template>
  <div class="emp-overtime">
    <PageHeader title="加班申请" icon="Timer" />

    <GlassCard title="提交加班" padding="lg">
      <el-form :model="form" label-width="100px" class="ot-form">
        <el-form-item label="加班日期">
          <el-date-picker v-model="form.date" type="date" style="width:100%" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.startTime" type="datetime" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" style="width:100%" />
        </el-form-item>
        <el-form-item label="加班原因">
          <el-input v-model="form.reason" type="textarea" :rows="3" />
        </el-form-item>
        <el-button type="primary" @click="submit">提交申请</el-button>
      </el-form>
    </GlassCard>

    <GlassCard title="我的加班记录" padding="lg" class="mt-16">
      <el-table :data="records" stripe>
        <el-table-column prop="date" label="日期" />
        <el-table-column prop="hours" label="小时" />
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
const form = reactive({ date: '', startTime: '', endTime: '', reason: '' })
const records = ref([])
onMounted(async () => { const r = await request.get('/overtime-requests', { headers }); records.value = r.records })
async function submit() { await request.post('/overtime-requests', form, { headers }); ElMessage.success('提交成功'); const r = await request.get('/overtime-requests', { headers }); records.value = r.records }
</script>

<style scoped>
.emp-overtime {
  animation: float-up 0.5s ease both;
}
.ot-form {
  max-width: 500px;
}
.mt-16 {
  margin-top: 16px;
}
</style>
