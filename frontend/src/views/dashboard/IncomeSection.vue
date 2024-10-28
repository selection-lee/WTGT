<!-- views/Dashboard/IncomeSection.vue -->
<template>
  <section class="mt-8">
    <h2 class="font-bold py-4 uppercase">Last 24h incomes</h2>
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
      <Card 
        v-for="income in incomes" 
        :key="income.id"
        variant="dark"
        noPadding
      >
        <div class="flex flex-row items-center">
          <div class="text-3xl p-4">💰</div>
          <div class="p-2">
            <p class="text-xl font-bold">{{ income.amount }}</p>
            <p class="text-gray-500 font-medium">{{ income.name }}</p>
            <p class="text-gray-500 text-sm">{{ income.date }}</p>
          </div>
        </div>
        <div class="border-t border-white/5 p-4">
          <Button variant="outline" size="sm" class="w-full">
            <template #icon>
              <Icons name="InformationCircleIcon" size="sm" />
            </template>
            Info
          </Button>
        </div>
      </Card>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import Card from '@/components/common/Card.vue'
import Button from '@/components/common/Button.vue'
import Icons from '@/components/common/Icons.vue'

const incomes = ref([
  {
    id: 1,
    amount: '348$',
    name: 'Amber Gates',
    date: '24 Nov 2022'
  },
  {
    id: 2,
    amount: '68$',
    name: 'Maia Kipper',
    date: '23 Nov 2022'
  },
  {
    id: 3,
    amount: '12$',
    name: 'Oprah Milles',
    date: '23 Nov 2022'
  },
  {
    id: 4,
    amount: '105$',
    name: 'Jonny Nite',
    date: '23 Nov 2022'
  },
  {
    id: 5,
    amount: '52$',
    name: 'Megane Baile',
    date: '22 Nov 2022'
  },
  {
    id: 6,
    amount: '28$',
    name: 'Tony Ankel',
    date: '22 Nov 2022'
  }
])
</script>