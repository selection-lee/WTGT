import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'

const routes = [
  {
    path: '/',
    name: 'dashboard',
    component: DashboardView
  }
  // 다른 라우트들...
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router