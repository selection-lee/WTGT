<!-- components/common/Button.vue -->
<template>
  <button 
    :class="[
      'transition duration-150 ease-in-out rounded-lg',
      'inline-flex items-center space-x-2',
      variantClasses[variant],
      sizeClasses[size],
      { 'opacity-50 cursor-not-allowed': disabled }
    ]"
    :disabled="disabled"
    @click="$emit('click')"
  >
    <slot name="icon"></slot>
    <span><slot></slot></span>
  </button>
</template>

<script setup>
const props = defineProps({
  variant: {
    type: String,
    default: 'primary',
    validator: (value) => ['primary', 'secondary', 'outline'].includes(value)
  },
  size: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

const variantClasses = {
  primary: 'bg-indigo-600 text-white hover:bg-indigo-700',
  secondary: 'bg-gray-600 text-white hover:bg-gray-700',
  outline: 'border border-indigo-600 text-indigo-600 hover:bg-indigo-50'
}

const sizeClasses = {
  sm: 'px-2 py-1 text-sm',
  md: 'px-4 py-2 text-base',
  lg: 'px-6 py-3 text-lg'
}
</script>