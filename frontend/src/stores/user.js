import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/utils/request'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const role = ref(localStorage.getItem('role') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const employeeId = ref(localStorage.getItem('employeeId') || '')
  const username = ref(localStorage.getItem('username') || '')

  async function login(form) {
    const data = await request.post('/auth/login', form)
    token.value = data.token
    role.value = data.role
    userId.value = data.userId
    employeeId.value = data.employeeId
    username.value = data.username
    localStorage.setItem('token', token.value)
    localStorage.setItem('role', role.value)
    localStorage.setItem('userId', userId.value)
    localStorage.setItem('employeeId', employeeId.value)
    localStorage.setItem('username', username.value)
  }

  function logout() {
    token.value = ''
    role.value = ''
    userId.value = ''
    employeeId.value = ''
    username.value = ''
    localStorage.clear()
  }

  return { token, role, userId, employeeId, username, login, logout }
})
