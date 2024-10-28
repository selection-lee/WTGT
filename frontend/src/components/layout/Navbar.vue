<template>
    <div class="bg-white/10 rounded-lg p-4">
      <!-- 로고 및 사용자 정보 -->
      <h1 class="font-bold text-lg lg:text-3xl bg-gradient-to-br from-white via-white/50 to-transparent bg-clip-text text-transparent">
        Dashboard<span class="text-indigo-400">.</span>
      </h1>
      <!-- 사용자 프로필 -->
      <div class="user-profile">
        <!-- 프로필 컨텐츠 -->
      </div>
      
      <!-- 네비게이션 메뉴 -->
      <div class="flex flex-col space-y-2 my-5">
        <nav-item v-for="item in menuItems" 
                  :key="item.title"
                  :icon="item.icon"
                  :title="item.title"
                  :description="item.description" />
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Navbar',
    data() {
      return {
        menuItems: [
          {
            title: 'Dashboard',
            description: 'Data overview',
            icon: 'home-icon'
          },
          // ... 기타 메뉴 아이템들
        ]
      }
    }
  }
  </script>