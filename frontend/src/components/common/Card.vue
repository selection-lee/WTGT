<!-- components/common/Card.vue -->
<template>
  <div :class="[
    'rounded-lg',
    variant === 'dark' ? 'bg-black/60 to-white/5' : 'bg-white/10', 
    noPadding ? '' : 'p-6'  // noPadding이 true일 때는 패딩 없음
  ]">
    <slot></slot>
  </div>
</template>

<script setup>
defineProps({
  variant: {
    type: String,
    default: 'default',
    validator: (value) => ['default', 'dark'].includes(value)
  },
  noPadding: {
    type: Boolean,
    default: false
  }
})
</script>