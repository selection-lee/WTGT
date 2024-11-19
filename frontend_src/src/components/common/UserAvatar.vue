<!-- components/common/UserAvatar.vue -->
<template>
  <div class="inline-flex space-x-3 items-center">
    <img 
      :src="image"
      :alt="name"
      class="rounded-full object-cover"
      :class="sizeClasses[size]"
    >
    <div v-if="showInfo">
      <p :class="[
        'font-medium leading-4',
        textClasses[size]
      ]">{{ name }}</p>
      <span v-if="description" class="text-slate-400 text-xs">
        {{ description }}
      </span>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  image: String,
  name: String,
  description: String,
  size: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  },
  showInfo: {
    type: Boolean,
    default: true
  }
})

const sizeClasses = {
  sm: 'w-8 h-8',
  md: 'w-10 h-10',
  lg: 'w-12 h-12'
}

const textClasses = {
  sm: 'text-sm',
  md: 'text-base',
  lg: 'text-lg'
}
</script>