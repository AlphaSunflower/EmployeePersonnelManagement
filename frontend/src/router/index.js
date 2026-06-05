import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue')
  },
  {
    path: '/employee',
    component: () => import('@/layouts/EmployeeLayout.vue'),
    meta: { requiresAuth: true, role: 'EMPLOYEE' },
    redirect: '/employee/dashboard',
    children: [
      { path: 'dashboard', name: 'EmpDashboard', component: () => import('@/views/employee/Dashboard.vue'), meta: { transition: 'page-fade' } },
      { path: 'profile', name: 'EmpProfile', component: () => import('@/views/employee/Profile.vue'), meta: { transition: 'page-fade' } },
      { path: 'attendance', name: 'EmpAttendance', component: () => import('@/views/employee/Attendance.vue'), meta: { transition: 'page-fade' } },
      { path: 'leave', name: 'EmpLeave', component: () => import('@/views/employee/Leave.vue'), meta: { transition: 'page-fade' } },
      { path: 'overtime', name: 'EmpOvertime', component: () => import('@/views/employee/Overtime.vue'), meta: { transition: 'page-fade' } },
      { path: 'makeup', name: 'EmpMakeup', component: () => import('@/views/employee/Makeup.vue'), meta: { transition: 'page-fade' } },
      { path: 'salary', name: 'EmpSalary', component: () => import('@/views/employee/Salary.vue'), meta: { transition: 'page-fade' } }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { transition: 'page-fade' } },
      { path: 'departments', name: 'Departments', component: () => import('@/views/admin/Departments.vue'), meta: { transition: 'page-fade' } },
      { path: 'positions', name: 'Positions', component: () => import('@/views/admin/Positions.vue'), meta: { transition: 'page-fade' } },
      { path: 'organization', name: 'Organization', component: () => import('@/views/admin/Organization.vue'), meta: { transition: 'page-fade' } },
      { path: 'employees', name: 'Employees', component: () => import('@/views/admin/Employees.vue'), meta: { transition: 'page-fade' } },
      { path: 'attendance', name: 'AdminAttendance', component: () => import('@/views/admin/Attendance.vue'), meta: { transition: 'page-fade' } },
      { path: 'leave-approval', name: 'LeaveApproval', component: () => import('@/views/admin/LeaveApproval.vue'), meta: { transition: 'page-fade' } },
      { path: 'overtime-approval', name: 'OvertimeApproval', component: () => import('@/views/admin/OvertimeApproval.vue'), meta: { transition: 'page-fade' } },
      { path: 'makeup-approval', name: 'MakeupApproval', component: () => import('@/views/admin/MakeupApproval.vue'), meta: { transition: 'page-fade' } },
      { path: 'salary-structures', name: 'SalaryStructures', component: () => import('@/views/admin/SalaryStructures.vue'), meta: { transition: 'page-fade' } },
      { path: 'employee-salaries', name: 'EmployeeSalaries', component: () => import('@/views/admin/EmployeeSalaries.vue'), meta: { transition: 'page-fade' } },
      { path: 'salary-calculate', name: 'SalaryCalculate', component: () => import('@/views/admin/SalaryCalculate.vue'), meta: { transition: 'page-fade' } },
      { path: 'salary-records', name: 'SalaryRecords', component: () => import('@/views/admin/SalaryRecords.vue'), meta: { transition: 'page-fade' } }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const store = useUserStore()
  if (to.path === '/login') {
    if (store.token) next(to.query.redirect || (store.role === 'ADMIN' ? '/admin' : '/employee'))
    else next()
    return
  }
  if (!store.token) { next('/login'); return }
  if (to.meta.role && to.meta.role !== store.role) { next('/login'); return }
  next()
})

export default router
