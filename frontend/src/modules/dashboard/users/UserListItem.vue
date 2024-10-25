<!-- modules/dashboard/users/UserListItem.vue -->
<template>
  <tr class="border-b border-gray-700">
    <td class="py-3 px-2 font-bold">
      <div class="inline-flex space-x-3 items-center">
        <span>
          <img
            :src="user.avatar || '/api/placeholder/32/32'"
            class="rounded-full w-8 h-8"
            :alt="user.name"
          >
        </span>
        <span>{{ user.name }}</span>
      </div>
    </td>
    <td class="py-3 px-2">{{ user.email }}</td>
    <td class="py-3 px-2">{{ user.group }}</td>
    <td class="py-3 px-2">
      <span
        :class="[
          'px-2 py-1 rounded-full text-xs',
          statusClasses[user.status.toLowerCase()]
        ]"
      >
        {{ user.status }}
      </span>
    </td>
    <td class="py-3 px-2">
      <div class="inline-flex items-center space-x-3">
        <button
          title="Edit"
          class="hover:text-white"
          @click="emit('edit', user)"
        >
          <AppIcon name="edit" size="sm" />
        </button>
        <button
          title="Edit password"
          class="hover:text-white"
          @click="emit('edit-password', user)"
        >
          <AppIcon name="password" size="sm" />
        </button>
        <button
          title="Suspend user"
          class="hover:text-white"
          @click="emit('suspend', user)"
        >
          <AppIcon name="suspend" size="sm" />
        </button>
      </div>
    </td>
  </tr>
</template>

<script setup>
import AppIcon from '@/components/common/atoms/AppIcon.vue'

const props = defineProps({
  user: {
    type: Object,
    required: true,
    validator: (user) => {
      return (
        typeof user.name === 'string' &&
        typeof user.email === 'string' &&
        typeof user.group === 'string' &&
        typeof user.status === 'string'
      )
    }
  }
})

const emit = defineEmits(['edit', 'edit-password', 'suspend'])

const statusClasses = {
  approved: 'bg-green-500/20 text-green-500',
  pending: 'bg-yellow-500/20 text-yellow-500',
  suspended: 'bg-red-500/20 text-red-500'
}
</script>