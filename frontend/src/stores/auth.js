// src/stores/auth.js

import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi } from '@/services/api' // 추가 예정

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token'))
  const refreshToken = ref(localStorage.getItem('refreshToken'))

  // 로그인
  const login = async (credentials) => {
    try {
      const response = await authApi.login(credentials)
      
      // 토큰 저장
      token.value = response.token
      refreshToken.value = response.refreshToken
      localStorage.setItem('token', response.token)
      localStorage.setItem('refreshToken', response.refreshToken)
      
      // 사용자 정보 저장
      user.value = response.user
      
      return response
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }

  // 로그아웃
  const logout = () => {
    user.value = null
    token.value = null
    refreshToken.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
  }

  // 토큰 갱신
  const refreshAccessToken = async () => {
    try {
      const response = await authApi.refresh(refreshToken.value)
      token.value = response.token
      localStorage.setItem('token', response.token)
      return response.token
    } catch (error) {
      console.error('Token refresh error:', error)
      logout()
      throw error
    }
  }

  return {
    user,
    token,
    refreshToken,
    login,
    logout,
    refreshAccessToken
  }
})