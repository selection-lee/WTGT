<!-- modules/dashboard/users/UserList.vue -->
<template>
  <section>
    <h1 class="font-bold py-4 uppercase">Last 24h users</h1>
    <AppTable
      :headers="headers"
      :items="users"
      class="overflow-x-scroll"
    >
      <template #name="{ item }">
        <div class="inline-flex space-x-3 items-center font-bold">
          <span>
            <img
              :src="item.avatar || '/api/placeholder/32/32'"
              class="rounded-full w-8 h-8"
              :alt="item.name"
            >
          </span>
          <span>{{ item.name }}</span>
        </div>
      </template>

      <template #status="{ item }">
        <span
          :class="[
            'px-2 py-1 rounded-full text-xs',
            statusClasses[item.status.toLowerCase()]
          ]"
        >
          {{ item.status }}
        </span>
      </template>

      <template #actions="{ item }">
        <div class="inline-flex items-center space-x-3">
          <button
            title="Edit"
            class="hover:text-white"
            @click="handleEdit(item)"
          >
            <AppIcon name="edit" size="sm" />
          </button>
          <button
            title="Edit password"
            class="hover:text-white"
            @click="handleEditPassword(item)"
          >
            <AppIcon name="password" size="sm" />
          </button>
          <button
            title="Suspend user"
            class="hover:text-white"
            @click="handleSuspend(item)"
          >
            <AppIcon name="suspend" size="sm" />
          </button>
        </div>
      </template>
    </AppTable>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/store/user'
import AppTable from '@/components/common/organisms/AppTable.vue'
import AppIcon from '@/components/common/atoms/AppIcon.vue'

const userStore = useUserStore()
const users = computed(() => userStore.recentUsers)

const headers = [
  { text: 'Name', value: 'name', class: 'rounded-l-lg' },
  { text: 'Email', value: 'email' },
  { text: 'Group', value: 'group' },
  { text: 'Status', value: 'status' },
  { text: 'Actions', value: 'actions', class: 'rounded-r-lg' }
]

const statusClasses = {
  approved: 'bg-green-500/20 text-green-500',
  pending: 'bg-yellow-500/20 text-yellow-500',
  suspended: 'bg-red-500/20 text-red-500'
}

const handleEdit = (user) => {
  // Edit 로직
}

const handleEditPassword = (user) => {
  // Password 변경 로직
}

const handleSuspend = (user) => {
  // Suspend 로직
}
</script>