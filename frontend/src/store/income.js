// store/income.js
import { defineStore } from 'pinia'

export const useIncomeStore = defineStore('income', {
  state: () => ({
    incomes: [
      {
        id: 1,
        amount: 348,
        name: 'Amber Gates',
        date: '2024-01-24'
      },
      {
        id: 2,
        amount: 68,
        name: 'Maia Kipper',
        date: '2024-01-23'
      },
      {
        id: 3,
        amount: 12,
        name: 'Oprah Milles',
        date: '2024-01-23'
      },
      {
        id: 4,
        amount: 105,
        name: 'Jonny Nite',
        date: '2024-01-23'
      },
      {
        id: 5,
        amount: 52,
        name: 'Megane Baile',
        date: '2024-01-22'
      },
      {
        id: 6,
        amount: 28,
        name: 'Tony Ankel',
        date: '2024-01-22'
      }
    ]
  }),

  actions: {
    async fetchIncomes() {
      // API 호출 로직
    },
    async getIncomeDetails(id) {
      // API 호출 로직
    }
  }
})