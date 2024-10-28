<template>
    <div class="bg-black/60 to-white/5 p-6 rounded-lg">
      <div class="flex flex-row space-x-4 items-center">
        <div class="text-white">
          <component :is="icon" class="w-10 h-10" />
        </div>
        <div>
          <p :class="`text-${color}-300 text-sm font-medium uppercase leading-4`">
            {{ title }}
          </p>
          <p class="text-white font-bold text-2xl inline-flex items-center space-x-2">
            <span>{{ value }}</span>
            <trend-icon v-if="showTrend" />
          </p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'StatCard',
    props: {
      title: String,
      value: String,
      icon: String,
      color: {
        type: String,
        default: 'indigo'
      },
      showTrend: {
        type: Boolean,
        default: true
      }
    }
  }
  </script>