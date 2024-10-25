<!-- components/common/organisms/AppNavigation.vue -->
<template>
  <nav class="flex flex-col space-y-2 my-5">
    <AppMenuItem
      v-for="item in items"
      :key="item.id"
      :icon="item.icon"
      :title="item.title"
      :description="item.description"
      :to="item.path"
      :badge="item.badge"
    />
  </nav>
</template>

<script setup>
import AppMenuItem from '../molecules/AppMenuItem.vue'

defineProps({
  items: {
    type: Array,
    required: true,
    validator: (items) => items.every(item => 
      typeof item.id === 'number' &&
      typeof item.icon === 'string' &&
      typeof item.title === 'string' &&
      typeof item.description === 'string' &&
      typeof item.path === 'string'
    )
  }
})
</script>