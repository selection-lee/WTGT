// stores/user.js
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: {
      name: 'Jim Smith',
      company: 'Pantazi LLC',
      image: '/user-profile.jpg'
    }
  }),
  
  actions: {
    // 필요한 경우 사용자 정보 업데이트 등의 액션 추가
    updateUser(userData) {
      this.user = { ...this.user, ...userData }
    }
  }
})