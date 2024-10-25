// views/dashboard/DashboardView.vue
<template>
  <div class="antialiased bg-black w-full min-h-screen text-slate-300 relative py-4">
    <div class="grid grid-cols-12 mx-auto gap-2 sm:gap-4 md:gap-6 lg:gap-10 xl:gap-14 max-w-7xl my-10 px-2">
      <AppSidebar />
      <AppContent>
        <StatsOverview />
        <IncomeList />
        <UserList />
      </AppContent>
    </div>
  </div>
</template>

<script setup>
import AppSidebar from '@/components/layout/AppSidebar.vue'
import AppContent from '@/components/layout/AppContent.vue'
import StatsOverview from '@/modules/dashboard/stats/StatsOverview.vue'
import IncomeList from '@/modules/dashboard/income/IncomeList.vue'
import UserList from '@/modules/dashboard/users/UserList.vue'
</script>