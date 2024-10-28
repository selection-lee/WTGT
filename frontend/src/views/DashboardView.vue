<template>
    <dashboard-layout>
      <statistics-cards />
      <income-list />
      <user-table />
    </dashboard-layout>
  </template>
  
  <script>
  import DashboardLayout from '@/components/layout/DashboardLayout.vue'
  import StatisticsCards from '@/components/dashboard/StatisticsCards.vue'
  import IncomeList from '@/components/dashboard/IncomeList.vue'
  import UserTable from '@/components/dashboard/UserTable.vue'
  
  export default {
    name: 'DashboardView',
    components: {
      DashboardLayout,
      StatisticsCards,
      IncomeList,
      UserTable
    }
  }
  </script>