<!-- src/views/Order/OrderView.vue -->
<template>
  <!-- 메인 컨테이너: flex 클래스를 통해 요소들을 수평 배치 -->
  <div class="flex">
    <div class="flex-1 p-5 text-white">
      <!-- 주문 폼 제목 -->
      <h2 class="text-xl font-bold mb-4">Order</h2>

      <!-- 전체 폼을 감싸는 컨테이너 -->
      <div class="flex bg-gray-950 text-white rounded-lg">
        <div class="flex-1 flex justify-center items-center p-5">
          <div class="bg-gray-950 p-8 rounded-lg w-2/3">
            
            <!-- 주문 폼 구성 -->
            <form class="space-y-4">
              <!-- 상품명 -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300"
                  >상품명</label
                >
                <input
                  type="text"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  placeholder="상품명을 입력하세요"
                />
              </div>
              <!-- 수량 -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">수량</label>
                <input
                  type="number"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  placeholder="수량을 입력하세요"
                />
              </div>

              <!-- 송장번호 -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300"
                  >송장번호</label
                >
                <input
                  type="text"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  placeholder="송장번호를 입력하세요"
                />
              </div>
              <!-- 완료 버튼 -->
              <button
                type="submit"
                class="w-full bg-[#FFB900] hover:bg-[#E5A700] text-black font-bold py-2 px-3 rounded text-lg mt-8"
              >
                완료
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "OrderView",
};
</script>
