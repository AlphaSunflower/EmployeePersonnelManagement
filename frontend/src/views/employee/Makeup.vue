<template>
  <div class="emp-makeup">
    <PageHeader title="补签申请" icon="EditPen" />

    <GlassCard title="提交补签" padding="lg">
      <el-form :model="form" label-width="100px" class="mu-form">
        <el-form-item label="补签日期">
          <el-date-picker v-model="form.date" type="date" style="width:100%" />
        </el-form-item>
        <el-form-item label="补签类型">
          <el-select v-model="form.type" style="width:100%">
            <el-option label="上班卡" value="CHECK_IN" />
            <el-option label="下班卡" value="CHECK_OUT" />
          </el-select>
        </el-form-item>
        <el-form-item label="补签原因">
          <el-input v-model="form.reason" type="textarea" :rows="3" />
        </el-form-item>
        <el-button type="primary" @click="submit">提交申请</el-button>
      </el-form>
    </GlassCard>

    <GlassCard title="我的补签记录" padding="lg" class="mt-16">
      <el-table :data="records" stripe>
        <el-table-column prop="date" label="日期" />
        <el-table-column label="类型">
          <template #default="{row}">{{ row.type==='CHECK_IN'?'上班卡':'下班卡' }}</template>
        </el-table-column>
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
const form = reactive({ date: '', type: 'CHECK_IN', reason: '' })
const records = ref([])
onMounted(async () => { const r = await request.get('/makeup-requests', { headers }); records.value = r.records })
async function submit() { await request.post('/makeup-requests', form, { headers }); ElMessage.success('提交成功'); const r = await request.get('/makeup-requests', { headers }); records.value = r.records }
</script>

<style scoped>
.emp-makeup {
  animation: float-up 0.5s ease both;
}
.mu-form {
  max-width: 500px;
}
.mt-16 {
  margin-top: 16px;
}
</style>
