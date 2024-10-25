<!-- modules/dashboard/stats/StatsCard.vue -->
<template>
  <div class="bg-black/60 to-white/5 p-6 rounded-lg">
    <div class="flex flex-row space-x-4 items-center">
      <div>
        <AppIcon
          :name="icon"
          size="lg"
          class="text-white"
        />
      </div>
      <div>
        <p :class="[colorClass, 'text-sm font-medium uppercase leading-4']">
          {{ title }}
        </p>
        <p class="text-white font-bold text-2xl inline-flex items-center space-x-2">
          <span>{{ value }}</span>
          <span>
            <AppIcon
              name="trend-up"
              size="base"
            />
          </span>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import AppIcon from '@/components/common/atoms/AppIcon.vue'

defineProps({
  title: {
    type: String,
    required: true
  },
  value: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    required: true
  },
  colorClass: {
    type: String,
    default: 'text-indigo-300'
  }
})
</script>