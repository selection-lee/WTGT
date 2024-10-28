<!-- // src/components/common/Icons.vue -->
<template>
  <component 
    :is="resolveIcon(name)" 
    :class="[
      sizeClasses[size],
      { 'group-hover:text-indigo-400': hover }
    ]"
    v-bind="$attrs"
  />
</template>

<script>
import {
  HomeIcon,
  DocumentIcon,
  UserIcon,
  CogIcon,
  ArrowUpRightIcon,
  InformationCircleIcon,
  PencilIcon,
  KeyIcon,
  NoSymbolIcon
} from '@heroicons/vue/24/outline'

// 컴포넌트 매핑
const ICON_COMPONENTS = {
  HomeIcon,
  DocumentIcon,
  UserIcon,
  CogIcon,
  ArrowUpRightIcon,
  InformationCircleIcon,
  PencilIcon,
  KeyIcon,
  NoSymbolIcon
}

export default {
  props: {
    name: {
      type: String,
      required: true,
      validator: (value) => Object.keys(ICON_COMPONENTS).includes(value)
    },
    size: {
      type: String,
      default: 'md',
      validator: (value) => ['sm', 'md', 'lg'].includes(value)
    },
    hover: {
      type: Boolean,
      default: false
    }
  },
  setup() {
    const sizeClasses = {
      sm: 'w-5 h-5',
      md: 'w-6 h-6',
      lg: 'w-10 h-10'
    }

    const resolveIcon = (name) => ICON_COMPONENTS[name]

    return {
      sizeClasses,
      resolveIcon
    }
  }
}
</script>