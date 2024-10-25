<!-- components/layout/AppSidebar.vue -->
<template>
  <aside class="bg-white/10 col-span-3 rounded-lg p-4">
    <h1 class="font-bold text-lg lg:text-3xl bg-gradient-to-br from-white via-white/50 to-transparent bg-clip-text text-transparent">
      Dashboard<span class="text-indigo-400">.</span>
    </h1>
    <p class="text-slate-400 text-sm mb-2">Welcome back,</p>
    
    <UserProfile />
    <hr class="my-2 border-slate-700">
    <AppNavigation :items="navigationItems" />
    
    <p class="text-sm text-center text-gray-600">v2.0.0.3 | &copy; {{ currentYear }} Pantazi Soft</p>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useNavigationStore } from '@/store/navigation'
import UserProfile from '@/modules/dashboard/users/UserProfile.vue'
import AppNavigation from '@/components/common/organisms/AppNavigation.vue'

const navigationStore = useNavigationStore()
const navigationItems = computed(() => navigationStore.items)
const currentYear = computed(() => new Date().getFullYear())
</script>

