<!-- src/views/Order/OrderView.vue -->
<template>
    <div class="flex h-screen bg-gray-900 text-white">

      <div class="flex-1 flex justify-center items-center p-10">
        <div class="bg-gray-800 p-8 rounded-lg w-1/3">
          <h3 class="text-2xl font-bold mb-6">Order</h3>
          <form class="space-y-6">
            <div class="flex items-center space-x-4">
              <label class="w-1/3">상품명</label>
              <input type="text" class="w-2/3 bg-gray-700 p-2 rounded" />
            </div>
            <div class="flex items-center space-x-4">
              <label class="w-1/3">수량</label>
              <input type="number" class="w-2/3 bg-gray-700 p-2 rounded" />
            </div>
            <div class="flex items-center space-x-4">
              <label class="w-1/3">송장번호</label>
              <input type="text" class="w-2/3 bg-gray-700 p-2 rounded" />
            </div>
            <button type="submit" class="w-full bg-pink-600 p-3 rounded mt-6 font-semibold">완료</button>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "OrderView",
  };
  </script>
  
  <style scoped>
  /* 스타일은 필요시 추가 */
  </style>
  