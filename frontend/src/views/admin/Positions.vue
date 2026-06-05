<template>
  <div class="admin-positions">
    <PageHeader title="岗位管理" icon="OfficeBuilding">
      <template #actions>
        <el-button type="primary" @click="openDialog()">新增岗位</el-button>
      </template>
    </PageHeader>

    <GlassCard padding="lg">
      <el-table :data="positions" stripe>
        <el-table-column prop="name" label="岗位名称" />
        <el-table-column label="所属部门">
          <template #default="{row}">{{ getDeptName(row.deptId) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{row}">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </GlassCard>

    <el-dialog v-model="dialogVisible" :title="editing.id ? '编辑岗位' : '新增岗位'" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="岗位名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="所属部门">
          <el-select v-model="form.deptId" placeholder="请选择部门" style="width:100%">
            <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位描述"><el-input v-model="form.description" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
const positions = ref([]);const deptList = ref([]);const dialogVisible = ref(false);const editing = ref({});const form = reactive({ name: '', deptId: null, description: '' })
onMounted(() => { load(); loadDepts() })
async function load() { const r = await request.get('/positions');positions.value = r }
async function loadDepts() { try { const r = await request.get('/departments/list'); deptList.value = r } catch (e) {} }
function getDeptName(id) { const d = deptList.value.find(d => d.id === id); return d ? d.name : id }
function openDialog(row) { editing.value = row || {}; if (row) { form.name = row.name;form.deptId = row.deptId;form.description = row.description } else { form.name = '';form.deptId = null;form.description = '' };dialogVisible.value = true }
async function save() { if (editing.value.id) await request.put(`/positions/${editing.value.id}`, form);else await request.post('/positions', form);ElMessage.success('保存成功');dialogVisible.value = false;load() }
async function del(id) { await ElMessageBox.confirm('确定删除？', '确认', { type: 'warning' });await request.delete(`/positions/${id}`);ElMessage.success('删除成功');load() }
</script>

<style scoped>
.admin-positions {
  animation: float-up 0.5s ease both;
}
</style>
