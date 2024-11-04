// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

// 메인 레이아웃과 페이지 컴포넌트들 import
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import DashboardView from '@/views/Dashboard/DashboardView.vue'
import RCcarView from '@/views/RC_car/RCcarView.vue' // RC 카 페이지 컴포넌트
import LoginView from '@/views/Auth/LoginView.vue'
import OrderView from '@/views/Order/OrderView.vue' 

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
        path: 'rc_car',
        name: 'rc_car',
        component: RCcarView,  
      },
      {
        path: 'monitoring',
        name: 'monitoring',
        component: UserSection
      },
      {
        path: 'order',
        name: 'order',
        component: OrderView
      },
      {
        path: 'statistics',
        name: 'statistics',
        component: IncomeSection
      },
      {
        path: 'users',
        name: 'users',
        component: UserSection  
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
  {
    // 로그인 페이지
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: {
      requiresAuth: false
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 필요한 경우 네비게이션 가드 추가
router.beforeEach((to, from, next) => {
  // // 예: 인증이 필요한 경우
  // // const isAuthenticated = localStorage.getItem('token')
  // // if (to.name !== 'login' && !isAuthenticated) next({ name: 'login' })
  // // else next()
  // next()

  // 네비게이션 가드 
  const token = localStorage.getItem('token')
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)

  if (requiresAuth && !token) {
    next('/login')
  } else if (token && to.path === '/login') {
    next('/')
  } else {
    next()
  }
})

export default router