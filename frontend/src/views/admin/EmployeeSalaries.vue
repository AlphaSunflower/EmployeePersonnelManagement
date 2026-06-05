<template>
  <div class="admin-es">
    <PageHeader title="员工薪酬标准" icon="Money">
      <template #actions>
        <el-button type="primary" @click="openDialog()">设置薪酬</el-button>
      </template>
    </PageHeader>

    <GlassCard padding="lg">
      <el-table :data="records" stripe>
        <el-table-column prop="employeeName" label="员工" />
        <el-table-column prop="basicSalary" label="基本工资" />
        <el-table-column prop="performanceSalary" label="绩效工资" />
        <el-table-column prop="subsidy" label="津贴补贴" />
        <el-table-column label="操作" width="200">
          <template #default="{row}">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </GlassCard>

    <el-dialog v-model="dialogVisible" :title="editing.id ? '编辑薪酬' : '设置薪酬'" width="450px">
      <el-form :model="form" label-width="120px">
        <el-form-item v-if="!editing.id" label="员工">
          <el-select v-model="form.employeeId" placeholder="请选择员工" style="width:100%" filterable>
            <el-option v-for="e in employeeList" :key="e.id" :label="`${e.name} (${e.phone || ''})`" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item v-else label="员工">
          <el-input :model-value="editing.employeeName" disabled />
        </el-form-item>
        <el-form-item label="基本工资"><el-input-number v-model="form.basicSalary" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="绩效工资"><el-input-number v-model="form.performanceSalary" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="津贴补贴"><el-input-number v-model="form.subsidy" :min="0" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';import request from '@/utils/request';import { ElMessage } from 'element-plus'
const records = ref([]);const employeeList = ref([]);const dialogVisible = ref(false);const editing = ref({});const form = reactive({ employeeId: null, basicSalary: 0, performanceSalary: 0, subsidy: 0 })
onMounted(() => { load(); loadEmployees() })
async function load() { const r = await request.get('/salary/employee-salaries');records.value = r.records }
async function loadEmployees() { try { const r = await request.get('/employees?size=200'); employeeList.value = r.records || [] } catch (e) {} }
function openDialog(row) {
  editing.value = row || {}
  if (row) { form.employeeId = row.employeeId; form.basicSalary = row.basicSalary; form.performanceSalary = row.performanceSalary; form.subsidy = row.subsidy }
  else { form.employeeId = null; form.basicSalary = 0; form.performanceSalary = 0; form.subsidy = 0 }
  dialogVisible.value = true
}
async function save() { if (editing.value.id) await request.put(`/salary/employee-salaries/${editing.value.id}`, form);else await request.post('/salary/employee-salaries', form);ElMessage.success('保存成功');dialogVisible.value = false;load() }
</script>

<style scoped>
.admin-es {
  animation: float-up 0.5s ease both;
}
</style>
