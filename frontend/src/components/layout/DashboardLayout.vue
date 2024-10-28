 <!-- src/components/layout/DashboardLayout.vue -->
<template>
  <div class="antialiased bg-black w-full min-h-screen text-slate-300 relative py-4">
    <div class="grid grid-cols-12 mx-auto gap-2 sm:gap-4 md:gap-6 lg:gap-10 xl:gap-14 max-w-7xl my-10 px-2">
      <Sidebar class="col-span-3" />
      <main class="bg-white/10 col-span-9 rounded-lg p-6">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'
</script>