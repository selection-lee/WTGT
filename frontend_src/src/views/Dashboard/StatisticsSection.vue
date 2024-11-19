<!-- views/Dashboard/StatisticsSection.vue -->
<template>
  <section>
    <h2 class="font-bold py-4 uppercase">Last 24h Statistics</h2>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <Card 
        v-for="stat in statistics" 
        :key="stat.title"
        variant="dark"
      >
        <div class="flex items-center space-x-4">
          <Icons :name="stat.icon" size="lg" />
          <div>
            <p :class="`text-${stat.color}-300 text-sm font-medium uppercase leading-4`">
              {{ stat.title }}
            </p>
            <p class="text-white font-bold text-2xl inline-flex items-center space-x-2">
              <span>{{ stat.value }}</span>
              <Icons name="ArrowUpRightIcon" size="sm" />
            </p>
          </div>
        </div>
      </Card>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import Card from '@/components/common/Card.vue'
import Icons from '@/components/common/Icons.vue'

const statistics = ref([
  {
    title: 'Users',
    value: '+28',
    color: 'indigo',
    icon: 'UserIcon'
  },
  {
    title: 'Income',
    value: '$2,873.88',
    color: 'teal',
    icon: 'DocumentIcon'
  },
  {
    title: 'Invoices',
    value: '+79',
    color: 'blue',
    icon: 'DocumentIcon'
  }
])
</script>