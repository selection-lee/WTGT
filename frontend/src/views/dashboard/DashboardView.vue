 <!-- views/Dashboard/DashboardView.vue -->
<template>
  <div>
    <StatisticsSection />
    <IncomeSection />
    <UserSection />
  </div>
</template>

<script setup>
import StatisticsSection from './StatisticsSection.vue'
import IncomeSection from './IncomeSection.vue'
import UserSection from './UserSection.vue'
</script>