<template>
  <div class="admin-ss">
    <PageHeader title="薪酬结构" icon="Money">
      <template #actions>
        <el-button type="primary" @click="openDialog()">新增</el-button>
      </template>
    </PageHeader>

    <GlassCard padding="lg">
      <el-table :data="structures" stripe>
        <el-table-column prop="name" label="结构名称" />
        <el-table-column prop="basicSalary" label="基本工资" />
        <el-table-column prop="performanceSalary" label="绩效工资" />
        <el-table-column prop="subsidy" label="津贴补贴" />
        <el-table-column label="操作" width="200">
          <template #default="{row}">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </GlassCard>

    <el-dialog v-model="dialogVisible" :title="editing.id ? '编辑' : '新增'" width="400px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="结构名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="基本工资"><el-input-number v-model="form.basicSalary" /></el-form-item>
        <el-form-item label="绩效工资"><el-input-number v-model="form.performanceSalary" /></el-form-item>
        <el-form-item label="津贴补贴"><el-input-number v-model="form.subsidy" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';import request from '@/utils/request';import { ElMessage, ElMessageBox } from 'element-plus'
const structures = ref([]);const dialogVisible = ref(false);const editing = ref({});const form = reactive({ name: '', basicSalary: 0, performanceSalary: 0, subsidy: 0 })
onMounted(load);async function load() { const r = await request.get('/salary/structures');structures.value = r }
function openDialog(row) { editing.value = row || {};if (row) Object.assign(form, row);else Object.keys(form).forEach(k => form[k] = (typeof form[k] === 'number' ? 0 : ''));dialogVisible.value = true }
async function save() { if (editing.value.id) await request.put(`/salary/structures/${editing.value.id}`, form);else await request.post('/salary/structures', form);ElMessage.success('保存成功');dialogVisible.value = false;load() }
async function del(id) { await ElMessageBox.confirm('确定删除？', '确认', { type: 'warning' });await request.delete(`/salary/structures/${id}`);ElMessage.success('删除成功');load() }
</script>

<style scoped>
.admin-ss {
  animation: float-up 0.5s ease both;
}
</style>
