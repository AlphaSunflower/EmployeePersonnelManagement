<template>
  <div class="admin-depts">
    <PageHeader title="部门管理" icon="OfficeBuilding">
      <template #actions>
        <el-button type="primary" @click="openDialog()">新增部门</el-button>
      </template>
    </PageHeader>

    <GlassCard padding="lg">
      <el-table :data="depts" stripe row-key="id">
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="sortOrder" label="排序" />
        <el-table-column label="操作" width="200">
          <template #default="{row}">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </GlassCard>

    <el-dialog v-model="dialogVisible" :title="editing.id ? '编辑部门' : '新增部门'" width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="部门名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="上级部门"><el-select v-model="form.parentId" clearable><el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" /></el-select></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
const depts = ref([]);const dialogVisible = ref(false);const editing = ref({});const form = reactive({ name: '', parentId: null, sortOrder: 0 })
onMounted(load);async function load() { const r = await request.get('/departments/list'); depts.value = r }
function openDialog(row) { editing.value = row || {};if (row) { form.name = row.name;form.parentId = row.parentId || null;form.sortOrder = row.sortOrder } else { form.name = '';form.parentId = null;form.sortOrder = 0 };dialogVisible.value = true }
async function save() { if (editing.value.id) await request.put(`/departments/${editing.value.id}`, form);else await request.post('/departments', form);ElMessage.success('保存成功');dialogVisible.value = false;load() }
async function del(id) { await ElMessageBox.confirm('确定删除该部门？', '确认', { type: 'warning' });await request.delete(`/departments/${id}`);ElMessage.success('删除成功');load() }
</script>

<style scoped>
.admin-depts {
  animation: float-up 0.5s ease both;
}
</style>
