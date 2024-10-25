<template>
    <div class="antialiased bg-black w-full min-h-screen text-slate-300 relative py-4">
      <div class="grid grid-cols-12 mx-auto gap-2 sm:gap-4 md:gap-6 lg:gap-10 xl:gap-14 max-w-7xl my-10 px-2">
        <!-- 사이드바 -->
        <Navbar class="col-span-3" />
        
        <!-- 메인 콘텐츠 -->
        <div id="content" class="bg-white/10 col-span-9 rounded-lg p-6">
          <slot></slot>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import Navbar from './Navbar.vue'
  
  export default {
    name: 'DashboardLayout',
    components: {
      Navbar
    }
  }
  </script>