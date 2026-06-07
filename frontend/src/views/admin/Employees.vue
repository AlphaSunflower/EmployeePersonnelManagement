<template>
  <div class="admin-employees">
    <PageHeader title="员工管理" icon="UserFilled">
      <template #actions>
        <el-button type="success" @click="openDialog()">新增员工</el-button>
      </template>
    </PageHeader>

    <GlassCard padding="lg">
      <el-row :gutter="12" class="filter-row">
        <el-col :span="6"><el-input v-model="search.keyword" placeholder="搜索姓名/手机/邮箱" @keyup.enter="load" /></el-col>
        <el-col :span="4"><el-select v-model="search.status" clearable placeholder="状态" @change="load"><el-option label="在职" value="ACTIVE" /><el-option label="离职" value="RESIGNED" /></el-select></el-col>
        <el-col :span="6">
          <el-button type="primary" @click="load">搜索</el-button>
          <el-button @click="exportRoster">导出花名册</el-button>
        </el-col>
      </el-row>
    </GlassCard>

    <GlassCard padding="lg" class="mt-16">
      <el-table :data="employees" stripe>
        <el-table-column label="姓名">
          <template #default="{row}">
            <div class="name-cell">
              <el-avatar :src="row.avatarUrl" :size="32">{{ row.name?.charAt(0) }}</el-avatar>
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="性别"><template #default="{row}">{{ row.gender === 1 ? '男' : '女' }}</template></el-table-column>
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="hireDate" label="入职日期" />
        <el-table-column label="状态">
          <template #default="{row}"><el-tag :type="row.status==='ACTIVE'?'success':row.status==='PROBATION'?'warning':'danger'">{{ row.status==='ACTIVE'?'在职':row.status==='PROBATION'?'试用期':'离职' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="{row}">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" @click="openChange(row)">异动</el-button>
            <el-button v-if="row.status !== 'RESIGNED'" size="small" type="danger" @click="del(row.id)">离职</el-button>
            <el-button v-else size="small" type="success" @click="rehire(row.id)">复职</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-pagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" layout="prev,pager,next" @current-change="load" />
      </template>
    </GlassCard>

    <el-dialog v-model="dialogVisible" :title="editing.id ? '编辑员工' : '新增员工'" width="500px">
      <el-form :model="form" label-width="110px">
        <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="性别"><el-select v-model="form.gender"><el-option label="男" :value="1" /><el-option label="女" :value="0" /></el-select></el-form-item>
        <el-form-item label="身份证号"><el-input v-model="form.idCard" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="入职日期"><el-date-picker v-model="form.hireDate" type="date" /></el-form-item>
        <el-form-item label="所属部门">
          <el-select v-model="form.deptId" placeholder="请选择部门" style="width:100%">
            <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属岗位">
          <el-select v-model="form.positionId" placeholder="请选择岗位" style="width:100%">
            <el-option v-for="p in posList" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="职级"><el-input v-model="form.level" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="changeVisible" title="员工异动" width="400px">
      <el-form :model="changeForm" label-width="100px">
        <el-form-item label="状态"><el-select v-model="changeForm.status"><el-option label="在职" value="ACTIVE" /><el-option label="试用期" value="PROBATION" /><el-option label="离职" value="RESIGNED" /></el-select></el-form-item>
        <el-form-item label="调至部门">
          <el-select v-model="changeForm.deptId" placeholder="请选择部门" style="width:100%">
            <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="调至岗位">
          <el-select v-model="changeForm.positionId" placeholder="请选择岗位" style="width:100%">
            <el-option v-for="p in posList" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="changeVisible=false">取消</el-button><el-button type="primary" @click="saveChange">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { downloadFile } from '@/utils/download'
import { ElMessage, ElMessageBox } from 'element-plus'
const employees = ref([]);const dialogVisible = ref(false);const changeVisible = ref(false);const editing = ref({});const changingId = ref(null)
const deptList = ref([]);const posList = ref([])
const search = reactive({ keyword: '', status: '' });const page = reactive({ current: 1, size: 10, total: 0 })
const form = reactive({ name: '', gender: 1, idCard: '', phone: '', email: '', hireDate: '', deptId: null, positionId: null, level: '' })
const changeForm = reactive({ status: 'ACTIVE', deptId: null, positionId: null })
onMounted(() => { load(); loadDepts(); loadPositions() })
async function load() { const r = await request.get('/employees', { params: { current: page.current, size: page.size, keyword: search.keyword || undefined, status: search.status || undefined } });employees.value = r.records;page.total = r.total }
async function loadDepts() { try { const r = await request.get('/departments/list'); deptList.value = r } catch (e) {} }
async function loadPositions() { try { const r = await request.get('/positions'); posList.value = r } catch (e) {} }
function openDialog(row) { editing.value = row || {};if (row) Object.assign(form, row);else Object.keys(form).forEach(k => form[k] = k === 'gender' ? 1 : (k === 'hireDate' ? '' : (typeof form[k] === 'number' ? null : '')));dialogVisible.value = true }
async function save() { if (editing.value.id) await request.put(`/employees/${editing.value.id}`, form);else await request.post('/employees', form);ElMessage.success('保存成功');dialogVisible.value = false;load() }
async function del(id) { await ElMessageBox.confirm('确定离职？', '确认', { type: 'warning' });await request.delete(`/employees/${id}`);ElMessage.success('操作成功');load() }
async function rehire(id) { await request.post(`/employees/${id}/change`, { status: 'ACTIVE' });ElMessage.success('已复职');load() }
function openChange(row) { changingId.value = row.id; changeForm.status = row.status; changeForm.deptId = null; changeForm.positionId = null; changeVisible.value = true }
async function saveChange() { await request.post(`/employees/${changingId.value}/change`, changeForm);ElMessage.success('异动成功');changeVisible.value = false;load() }
async function exportRoster() {
  try {
    await downloadFile('/employees/export', {}, '员工花名册.xlsx')
    ElMessage.success('导出成功')
  } catch (e) { ElMessage.error('导出失败') }
}
</script>

<style scoped>
.admin-employees { animation: float-up 0.5s ease both; }
.filter-row { align-items: center; }
.mt-16 { margin-top: 16px; }
.name-cell { display: flex; align-items: center; gap: var(--space-3); }
</style>
