// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

// 메인 레이아웃과 페이지 컴포넌트들 import
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import DashboardView from '@/views/Dashboard/DashboardView.vue'
import RCcarView from '@/views/RC_car/RCcarView.vue' // RC 카 페이지 컴포넌트
import LoginView from '@/views/Auth/LoginView.vue'
import SignUpView from '@/views/Auth/SignUpView.vue'
import OrderView from '@/views/Order/OrderView.vue' 
import UserView from '@/views/User/UserView.vue' 
import MonitoringView from '@/views/Monitoring/MonitoringView.vue' 

import IncomeSection from '@/views/Dashboard/IncomeSection.vue'
import TestView from '../views/RC_car/TestView.vue'

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
        component: MonitoringView
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
        path: 'user',
        name: 'user',
        component: UserView
      },
    ]
  },
  {
    // 로그인 페이지
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/signup',
    name: 'signup',
    component: SignUpView,
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
  // 개발 환경에서 인증 비활성화 확인
  if (import.meta.env.VITE_DISABLE_AUTH === 'true') {
    return next()
  }

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