<!-- src/views/StockMonitoring/StockMonitoringView.vue -->
<template>
  <div class="flex">
    <!-- Main Content -->
    <main class="flex-1 p-5 text-white">
      <!-- Header -->
      <h2 class="text-xl font-bold mb-4">Stock Monitoring</h2>

      <!-- 실시간 입고 상품 테이블 -->
      <section class="mb-8">
        <div class="bg-gray-950 p-8 rounded-lg">
          <h3 class="text-xl font-semibold mb-4">실시간 입고 상품</h3>
          <table class="w-full bg-gray-700 rounded-lg text-center">
            <thead>
              <tr class="bg-gray-800">
                <th class="py-2">ID</th>
                <th>상품</th>
                <th>수량</th>
                <th>상태</th>
                <th>적재 위치</th>
                <th>Bot</th>
              </tr>
            </thead>
            <tbody>
              <!-- 데이터 행 (여기서는 예시 데이터로 작성) -->
              <tr
                v-for="item in stockData"
                :key="item.id"
                class="border-t border-gray-500"
              >
                <td class="py-2">{{ item.id }}</td>
                <td>{{ item.product }}</td>
                <td>{{ item.quantity }}</td>
                <td
                  :class="
                    item.status === '이동 중'
                      ? 'text-blue-500'
                      : 'text-green-500'
                  "
                >
                  {{ item.status }}
                </td>
                <td>{{ item.location }}</td>
                <td>{{ item.bot }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- 입고 단계별 건수 -->
      <section class="bg-gray-950 p-8 rounded-lg">
        <h3 class="text-xl font-semibold mb-4">입고 단계별 건수</h3>

        <!-- 테이블 형식으로 수정 및 진행 퍼센트 아이콘 추가 -->
        <table class="w-full bg-gray-700 rounded-lg text-center">
          <thead>
            <tr class="bg-gray-800">
              <th class="py-2 text-gray-400">대기 중</th>
              <th class="py-2 text-gray-400">이동 중</th>
              <th class="py-2 text-gray-400">적재 완료</th>
              <th class="py-2 text-gray-400">인식 실패 / 미등록</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="py-2 text-xl font-bold">
                {{ counts.pending }}
                <!-- 진행 퍼센트 아이콘 -->
                <div class="relative w-full bg-gray-600 rounded h-2 mt-2">
                  <div
                    class="absolute left-0 top-0 h-2 bg-yellow-500 rounded"
                    :style="{ width: pendingPercent + '%' }"
                  ></div>
                </div>
                <p class="text-sm text-gray-400 mt-1">{{ pendingPercent }}%</p>
              </td>
              <td class="py-4 text-xl font-bold">
                {{ counts.inTransit }}
                <div class="relative w-full bg-gray-600 rounded h-2 mt-2">
                  <div
                    class="absolute left-0 top-0 h-2 bg-blue-500 rounded"
                    :style="{ width: inTransitPercent + '%' }"
                  ></div>
                </div>
                <p class="text-sm text-gray-400 mt-1">
                  {{ inTransitPercent }}%
                </p>
              </td>
              <td class="py-4 text-xl font-bold">
                {{ counts.completed }}
                <div class="relative w-full bg-gray-600 rounded h-2 mt-2">
                  <div
                    class="absolute left-0 top-0 h-2 bg-green-500 rounded"
                    :style="{ width: completedPercent + '%' }"
                  ></div>
                </div>
                <p class="text-sm text-gray-400 mt-1">
                  {{ completedPercent }}%
                </p>
              </td>
              <td class="py-4 text-xl font-bold">
                {{ counts.failed }}
                <div class="relative w-full bg-gray-600 rounded h-2 mt-2">
                  <div
                    class="absolute left-0 top-0 h-2 bg-red-500 rounded"
                    :style="{ width: failedPercent + '%' }"
                  ></div>
                </div>
                <p class="text-sm text-gray-400 mt-1">{{ failedPercent }}%</p>
              </td>
            </tr>
          </tbody>
        </table>
      </section>
    </main>
  </div>
</template>

<script>
import { ref, computed } from "vue";

export default {
  name: "StockMonitoringView",
  setup() {
    // 입고 상품 데이터
    const stockData = ref([
      {
        id: 19,
        product: "conduct",
        quantity: 8,
        status: "이동 중",
        location: "A2",
        bot: "Bot 1",
      },
      {
        id: 18,
        product: "conduct",
        quantity: 2,
        status: "이동 중",
        location: "B1",
        bot: "Bot 2",
      },
      {
        id: 17,
        product: "conduct",
        quantity: 1,
        status: "적재 완료",
        location: "A3",
        bot: "Bot 1",
      },
      // ... 추가 데이터
    ]);

    // 입고 단계별 건수 데이터
    const counts = ref({
      pending: 3,
      inTransit: 2,
      completed: 17,
      failed: 1,
    });

    // 전체 입고 건수 계산
    const totalItems = computed(() => {
      return (
        counts.value.pending +
        counts.value.inTransit +
        counts.value.completed +
        counts.value.failed
      );
    });

    // 각 상태별 진행 퍼센트 계산
    const pendingPercent = computed(() =>
      Math.round((counts.value.pending / totalItems.value) * 100)
    );
    const inTransitPercent = computed(() =>
      Math.round((counts.value.inTransit / totalItems.value) * 100)
    );
    const completedPercent = computed(() =>
      Math.round((counts.value.completed / totalItems.value) * 100)
    );
    const failedPercent = computed(() =>
      Math.round((counts.value.failed / totalItems.value) * 100)
    );

    return {
      stockData,
      counts,
      pendingPercent,
      inTransitPercent,
      completedPercent,
      failedPercent,
    };
  },
};
</script>

<style scoped></style>
