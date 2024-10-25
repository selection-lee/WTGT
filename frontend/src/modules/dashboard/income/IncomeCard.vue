<!-- modules/dashboard/income/IncomeCard.vue -->
<template>
  <div class="bg-black/60 to-white/5 rounded-lg">
    <div class="flex flex-row items-center">
      <div class="text-3xl p-4">💰</div>
      <div class="p-2">
        <p class="text-xl font-bold">{{ formatCurrency(amount) }}</p>
        <p class="text-gray-500 font-medium">{{ name }}</p>
        <p class="text-gray-500 text-sm">{{ formatDate(date) }}</p>
      </div>
    </div>
    <div class="border-t border-white/5 p-4">
      <a
        href="#"
        class="inline-flex space-x-2 items-center text-center"
        @click.prevent="emit('info', id)"
      >
        <AppIcon name="info" size="base" />
        <span>Info</span>
      </a>
    </div>
  </div>
</template>

<script setup>
import { useFormatter } from '@/composables/useFormatter'
import AppIcon from '@/components/common/atoms/AppIcon.vue'

const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  amount: {
    type: Number,
    required: true
  },
  name: {
    type: String,
    required: true
  },
  date: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['info'])

const { formatCurrency, formatDate } = useFormatter()
</script>