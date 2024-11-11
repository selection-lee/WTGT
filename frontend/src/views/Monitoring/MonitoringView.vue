<!-- src/views/StockMonitoring/StockMonitoringView.vue -->
<template>
  <div class="flex">
    <!-- Main Content -->
    <main class="flex-1 p-5 text-white">
      <!-- Header -->
      <h2 class="text-xl font-bold mb-4">Stock Monitoring</h2>

      <!-- 실시간 입고 상품 테이블 -->
      <section class="mb-8">
        <div class="bg-gray-950 p-5 rounded-lg">
          <h3 class="text-xl font-semibold text-gray-300 mb-4">실시간 입고 상품</h3>
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
      <section class="bg-gray-950 p-5 rounded-lg">
        <h3 class="text-xl font-semibold text-gray-300 mb-4">입고 단계별 건수</h3>

        <!-- 테이블, 진행 퍼센트 아이콘 -->
        <table class="w-full bg-gray-700 rounded-lg text-center table-fixed">
          <thead>
            <tr class="bg-gray-800">
              <th class="py-2 w-1/4">대기 중</th>
              <th class="py-2 w-1/4">이동 중</th>
              <th class="py-2 w-1/4">적재 완료</th>
              <th class="py-2 w-1/4">인식 실패 / 미등록</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="py-2 text-xl font-bold">
                {{ counts.pending }}
                <p class="text-sm text-yellow-500 mt-1">{{ pendingPercent }}%</p>
              </td>
              <td class="py-2 text-xl font-bold">
                {{ counts.inTransit }}
                <p class="text-sm text-blue-500 mt-1">
                  {{ inTransitPercent }}%
                </p>
              </td>
              <td class="py-2 text-xl font-bold">
                {{ counts.completed }}
                <p class="text-sm text-green-500 mt-1">
                  {{ completedPercent }}%
                </p>
              </td>
              <td class="py-2 text-xl font-bold">
                {{ counts.failed }}
                <p class="text-sm text-red-500 mt-1">{{ failedPercent }}%</p>
              </td>
            </tr>
            <tr>
              <td colspan="4" class="py-2">
                <!-- 통합 진행 퍼센트 아이콘 -->
                <div class="relative w-full bg-gray-600 rounded h-4 mt-2 flex">
                  <!-- 각각의 비율을 너비로 적용한 진행 바 -->
                  <div
                    class="h-4 bg-yellow-500"
                    :style="{ width: pendingPercent + '%' }"
                  >
                </div>
                  <div
                    class="h-4 bg-blue-500"
                    :style="{ width: inTransitPercent + '%' }"
                  ></div>
                  <div
                    class="h-4 bg-green-500"
                    :style="{ width: completedPercent + '%' }"
                  ></div>
                  <div
                    class="h-4 bg-red-500"
                    :style="{ width: failedPercent + '%' }"
                  ></div>
                </div>
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
