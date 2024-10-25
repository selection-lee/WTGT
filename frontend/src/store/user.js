// store/user.js 업데이트
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    currentUser: {
      id: 1,
      name: 'Jim Smith',
      email: 'jim.smith@example.com',
      company: 'Pantazi LLC',
      avatar: null,
      role: 'admin'
    },
    recentUsers: [
      {
        id: 1,
        name: 'Thai Mei',
        email: 'thai.mei@abc.com',
        group: 'User',
        status: 'Approved',
        avatar: null
      },
      {
        id: 2,
        name: 'Marquez Spineli',
        email: 'marquez.spineli@cba.com',
        group: 'User',
        status: 'Approved',
        avatar: null
      },
      {
        id: 3,
        name: 'Mark Spike',
        email: 'mark.spike@abc.com',
        group: 'Administrator',
        status: 'Approved',
        avatar: null
      }
    ]
  }),

  actions: {
    async fetchRecentUsers() {
      try {
        // API 호출 로직
      } catch (error) {
        console.error('Error fetching recent users:', error)
        throw error
      }
    },

    async updateUser(userId, data) {
      try {
        // API 호출 로직
      } catch (error) {
        console.error('Error updating user:', error)
        throw error
      }
    },

    async suspendUser(userId) {
      try {
        // API 호출 로직
      } catch (error) {
        console.error('Error suspending user:', error)
        throw error
      }
    },

    async updateUserPassword(userId) {
      try {
        // API 호출 로직
      } catch (error) {
        console.error('Error updating user password:', error)
        throw error
      }
    }
  }
})