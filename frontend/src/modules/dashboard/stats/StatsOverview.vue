<!-- modules/dashboard/stats/StatsOverview.vue -->
<template>
  <section>
    <h1 class="font-bold py-4 uppercase">Last 24h Statistics</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <StatsCard
        v-for="stat in statistics"
        :key="stat.id"
        v-bind="stat"
      />
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useStatsStore } from '@/store/stats'
import StatsCard from './StatsCard.vue'

const statsStore = useStatsStore()
const statistics = computed(() => statsStore.statistics)
</script>