// src/stores/auth.js

import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi } from '@/services/api'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token'))
  const isAuthenticated = ref(!!token.value)

  // 로그인
  const login = async (credentials) => {
    try {
      const response = await authApi.login(credentials)
      // 토큰 저장 - Authorization 헤더에서 JWT 토큰 추출
      const authHeader = response.headers.get('Authorization')
      
      if (authHeader && authHeader.startsWith('Bearer ')) {
        const newToken = authHeader.substring(7)
        token.value = newToken
        localStorage.setItem('token', newToken)
        isAuthenticated.value = true

        // 사용자 정보 저장
        user.value = {
          username: credentials.username
        }

        return true
      }
      throw new Error('토큰이 없습니다')
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }

  // 로그아웃
  const logout = () => {
    user.value = null
    token.value = null
    isAuthenticated.value = false
    localStorage.removeItem('token')
  }

  // 회원가입
  const signup = async (userData) => {
    try {
      await authApi.signup(userData)
      return true
    } catch (err) {
      console.error('회원가입 에러', err)
      throw err
    }
  }

  return {
    user,
    token,
    isAuthenticated,
    login,
    logout,
    signup
  }
})