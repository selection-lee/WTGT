<!-- components/layout/Sidebar.vue -->
<template>
  <Card>
    <!-- Logo -->
    <h1 class="font-bold text-lg lg:text-3xl bg-gradient-to-br from-white via-white/50 to-transparent bg-clip-text text-transparent">
      Dashboard<span class="text-indigo-400">.</span>
    </h1>
    <p class="text-slate-400 text-sm mb-2">Welcome back,</p>

    <!-- User Profile -->
    <a href="#" class="hover:bg-white/10 transition duration-150 ease-linear rounded-lg group w-full py-3 px-2">
      <UserAvatar 
        :image="user.image"
        :name="user.name"
        :description="user.company"
      />
    </a>

    <hr class="my-2 border-slate-700">

    <!-- Navigation -->
    <nav class="flex flex-col space-y-2 my-5">
      <template v-for="item in menuItems" :key="item.name">
        <a 
          :href="item.path"
          class="hover:bg-white/10 transition duration-150 ease-linear rounded-lg py-3 px-2 group"
        >
          <div class="relative flex items-center space-x-2">
            <Icons :name="item.icon" hover size="md" />
            <div>
              <p class="font-bold text-base lg:text-lg text-slate-200 leading-4 group-hover:text-indigo-400">
                {{ item.name }}
              </p>
              <p class="text-slate-400 text-sm hidden md:block">
                {{ item.description }}
              </p>
            </div>
            <div 
              v-if="item.badge" 
              class="absolute -top-3 -right-3 md:top-0 md:right-0 px-2 py-1.5 rounded-full bg-indigo-800 text-xs font-mono font-bold"
            >
              {{ item.badge }}
            </div>
          </div>
        </a>
      </template>
    </nav>

    <p class="text-sm text-center text-gray-600">v2.0.0.3 | &copy; 2022 Pantazi Soft</p>
  </Card>
</template>

<script setup>
import { ref } from 'vue'
import Card from '../common/Card.vue'
import UserAvatar from '../common/UserAvatar.vue'
import Icons from '../common/Icons.vue'

const user = ref({
  image: '/user-profile.jpg',
  name: 'Jim Smith',
  company: 'Pantazi LLC'
})

const menuItems = ref([
  {
    name: 'Dashboard',
    icon: 'HomeIcon',
    description: 'Data overview',
    path: '/'
  },
  {
    name: 'Invoices',
    icon: 'DocumentIcon',
    description: 'Manage invoices',
    path: '/invoices',
    badge: 23
  },
  {
    name: 'Users',
    icon: 'UserIcon',
    description: 'Manage users',
    path: '/users'
  },
  {
    name: 'Settings',
    icon: 'CogIcon',
    description: 'Edit settings',
    path: '/settings'
  }
])
</script>