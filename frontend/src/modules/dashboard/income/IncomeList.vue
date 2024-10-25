<!-- modules/dashboard/income/IncomeList.vue -->
<template>
  <section>
    <h1 class="font-bold py-4 uppercase">Last 24h incomes</h1>
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
      <IncomeCard
        v-for="income in incomes"
        :key="income.id"
        v-bind="income"
        @info="handleIncomeInfo"
      />
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useIncomeStore } from '@/store/income'
import IncomeCard from './IncomeCard.vue'

const incomeStore = useIncomeStore()
const incomes = computed(() => incomeStore.incomes)

const handleIncomeInfo = async (id) => {
  await incomeStore.getIncomeDetails(id)
  // 추가적인 로직 (모달 표시 등)
}
</script>