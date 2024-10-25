// store/stats.js
import { defineStore } from 'pinia'

export const useStatsStore = defineStore('stats', {
  state: () => ({
    statistics: [
      {
        id: 1,
        title: 'Users',
        value: '+28',
        icon: 'users',
        colorClass: 'text-indigo-300'
      },
      {
        id: 2,
        title: 'Income',
        value: '$2,873',
        icon: 'money',
        colorClass: 'text-teal-300'
      },
      {
        id: 3,
        title: 'Invoices',
        value: '+79',
        icon: 'document',
        colorClass: 'text-blue-300'
      }
    ]
  }),

  actions: {
    async fetchStatistics() {
      // API 호출 로직
    }
  }
})