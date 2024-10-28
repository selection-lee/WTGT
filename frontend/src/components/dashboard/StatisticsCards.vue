<template>
    <div id="24h">
      <h1 class="font-bold py-4 uppercase">Last 24h Statistics</h1>
      <div class="grid gird-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <stat-card v-for="stat in statistics" 
                  :key="stat.title"
                  :title="stat.title"
                  :value="stat.value"
                  :icon="stat.icon"
                  :color="stat.color" />
      </div>
    </div>
  </template>
  
  <script>
  import StatCard from '../common/StatCard.vue'
  
  export default {
    name: 'StatisticsCards',
    components: {
      StatCard
    },
    data() {
      return {
        statistics: [
          {
            title: 'Users',
            value: '+28',
            icon: 'user-icon',
            color: 'indigo'
          },
          // ... 기타 통계 데이터
        ]
      }
    }
  }
  </script>