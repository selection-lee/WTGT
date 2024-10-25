<!-- modules/dashboard/users/UserProfile.vue -->
<template>
  <div class="flex flex-col space-y-2 md:space-y-0 md:flex-row mb-5 items-center md:space-x-2 hover:bg-white/10 group transition duration-150 ease-linear rounded-lg group w-full py-3 px-2">
    <div>
      <img
        :src="currentUser.avatar || '/api/placeholder/40/40'"
        class="rounded-full w-10 h-10 relative object-cover"
        :alt="currentUser.name"
      >
    </div>
    <div>
      <p class="font-medium group-hover:text-indigo-400 leading-4">
        {{ currentUser.name }}
      </p>
      <span class="text-xs text-slate-400">{{ currentUser.company }}</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()
const currentUser = computed(() => userStore.currentUser)
</script>