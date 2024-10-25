<!-- components/common/molecules/AppMenuItem.vue -->
<template>
  <router-link
    :to="to"
    class="hover:bg-white/10 transition duration-150 ease-linear rounded-lg py-3 px-2 group"
  >
    <div class="relative flex flex-col space-y-2 md:flex-row md:space-y-0 space-x-2 items-center">
      <div>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="w-6 h-6 group-hover:text-indigo-400"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            :d="icon"
          />
        </svg>
      </div>
      <div>
        <p class="font-bold text-base lg:text-lg text-slate-200 leading-4 group-hover:text-indigo-400">
          {{ title }}
        </p>
        <p class="text-slate-400 text-sm hidden md:block">{{ description }}</p>
      </div>
      <div
        v-if="badge"
        class="absolute -top-3 -right-3 md:top-0 md:right-0 px-2 py-1.5 rounded-full bg-indigo-800 text-xs font-mono font-bold"
      >
        {{ badge }}
      </div>
    </div>
  </router-link>
</template>

<script setup>
defineProps({
  icon: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    required: true
  },
  to: {
    type: [String, Object],
    required: true
  },
  badge: {
    type: [String, Number],
    default: null
  }
})
</script>