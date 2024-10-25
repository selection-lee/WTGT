// src/store/dashboard.js
import { defineStore } from 'pinia'

export const useStatisticsStore = defineStore('statistics', {
  state: () => ({
    statistics: [
      {
        id: 1,
        title: 'Users',
        value: '+28',
        icon: 'M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z',
        colorClass: 'text-indigo-300'
      },
      {
        id: 2,
        title: 'Income',
        value: '$2,873.88',
        icon: 'M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33M21 12a9 9 0 11-18 0 9 9 0 0118 0z',
        colorClass: 'text-teal-300'
      },
      {
        id: 3,
        title: 'Invoices',
        value: '+79',
        icon: 'M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m2.25 0H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z',
        colorClass: 'text-blue-300'
      }
    ],
    recentIncomes: [
      {
        id: 1,
        amount: 348,
        name: 'Amber Gates',
        date: '24 Nov 2022'
      },
      // ... 다른 income 데이터
    ],
    recentUsers: [
      {
        id: 1,
        name: 'Thai Mei',
        email: 'thai.mei@abc.com',
        group: 'User',
        status: 'Approved',
        avatar: '/api/placeholder/32/32'
      },
      // ... 다른 user 데이터
    ]
  }),
  
  actions: {
    async fetchStatistics() {
      // API 호출 로직
    },
    async fetchRecentIncomes() {
      // API 호출 로직
    },
    async fetchRecentUsers() {
      // API 호출 로직
    }
  }
})

export const useNavigationStore = defineStore('navigation', {
  state: () => ({
    menuItems: [
      {
        id: 1,
        icon: 'M2.25 12l8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25',
        title: 'Dashboard',
        description: 'Data overview',
        path: '/dashboard'
      },
      // ... 다른 메뉴 아이템
    ]
  })
})