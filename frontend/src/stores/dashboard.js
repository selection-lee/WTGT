// stores/dashboard.js
import { defineStore } from 'pinia'

export const useDashboardStore = defineStore('dashboard', {
  state: () => ({
    statistics: {
        title: 'Users',
        value: '+28',
        color: 'indigo',
        icon: 'UserIcon'
    },
    incomes: [
        {
            id: 1,
            amount: '348$',
            name: 'Amber Gates',
            date: '24 Nov 2022'
          },
    ],
    users: [
        {
            name: 'Thai Mei',
            email: 'thai.mei@abc.com',
            group: 'User',
            status: 'Approved',
            image: '...'
          },
    ]
  }),

  actions: {
    async fetchDashboardData() {
      try {
        // API 호출 로직
        // const response = await api.get('/dashboard/statistics')
        // this.statistics = response.data.statistics
      } catch (error) {
        console.error('Error fetching dashboard data:', error)
      }
    }
  }
})