// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

// 메인 레이아웃과 페이지 컴포넌트들 import
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import DashboardView from '@/views/Dashboard/DashboardView.vue'
import UserSection from '@/views/Dashboard/UserSection.vue'
import IncomeSection from '@/views/Dashboard/IncomeSection.vue'

const routes = [
  {
    path: '/',
    component: DashboardLayout,
    children: [
      {
        path: '',
        name: 'dashboard',
        component: DashboardView,
      },
      {
        path: 'users',
        name: 'users',
        component: UserSection  
      },
      {
        path: 'invoices',
        name: 'invoices',
        component: IncomeSection
      },
    //   {
    //     path: 'settings',
    //     name: 'settings',
    //     component: () => import('@/views/Settings.vue'),  // 아직 만들지 않은 컴포넌트
    //   }
    ]
  },
//   // 에러 페이지
//   {
//     path: '/:pathMatch(.*)*',
//     name: 'not-found',
//     component: () => import('@/views/NotFound.vue')  // 404 페이지
//   }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 필요한 경우 네비게이션 가드 추가
router.beforeEach((to, from, next) => {
  // 예: 인증이 필요한 경우
  // const isAuthenticated = localStorage.getItem('token')
  // if (to.name !== 'login' && !isAuthenticated) next({ name: 'login' })
  // else next()
  next()
})

export default router