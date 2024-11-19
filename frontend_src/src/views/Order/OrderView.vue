<!-- src/views/Order/OrderView.vue -->
<template>
  <div class="flex">
    <div class="flex-1 p-5 text-white">
      <h2 class="text-xl font-bold mb-4">Order</h2>

      <div class="flex bg-gray-950 text-white rounded-lg">
        <div class="flex-1 flex justify-center items-center p-5">
          <div class="bg-gray-950 p-8 rounded-lg w-2/3">
            <!-- form에 submit 이벤트 핸들러 추가 -->
            <form @submit.prevent="handleSubmit" class="space-y-4">
              <!-- 카테고리 선택 -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">카테고리</label>
                <select
                  v-model="formData.categoryName"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  required
                >
                  <option value="" disabled>카테고리를 선택하세요</option>
                  <!-- categories 객체를 이용해 옵션 생성 -->
                  <option
                    v-for="(korName, engName) in orderStore.categories"
                    :key="engName"
                    :value="engName"
                  >
                    {{ korName }}
                  </option>
                </select>
              </div>

              <!-- 송장번호 -->
              <div class="flex flex-col space-y-2">
                  <label class="text-lg font-bold text-gray-300">송장번호</label>
                  <input
                  v-model="formData.invoiceNumber"
                  type="text"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  placeholder="송장번호를 입력하세요"
                  required
                  />
              </div>

              <!-- 물품명 -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">물품명</label>
                <input
                  v-model="formData.productName"
                  type="text"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  placeholder="물품명을 입력하세요"
                  required
                />
              </div>

              <!-- 수량 -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">수량</label>
                <input
                  v-model.number="formData.productQuantity"
                  type="number"
                  min="1"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  placeholder="수량을 입력하세요"
                  required
                />
              </div>

              <!-- 단가 -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">단가</label>
                <input
                  v-model.number="formData.unitPrice"
                  type="number"
                  min="0"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  placeholder="단가를 입력하세요"
                  required
                />
              </div>

              <!-- 총 금액 (자동 계산) -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">총 금액</label>
                <input
                  :value="totalAmount"
                  type="number"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  readonly
                />
              </div>

              <!-- 입고 예정일 -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">입고 예정일</label>
                <input
                  v-model="formData.expectedArrivalDate"
                  type="date"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  required
                />
              </div>

              <button
                type="submit"
                class="w-full bg-[#FFB900] hover:bg-[#E5A700] text-black font-bold py-2 px-3 rounded text-lg mt-8"
              >
                발주 등록
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useOrderStore } from '@/stores/order'

export default {
  name: "OrderView",
  
  setup() {
    const orderStore = useOrderStore()
    return { orderStore }
  },

  data() {
    return {
      formData: {
        categoryName: '',
        productName: '',
        productQuantity: null,
        unitPrice: null,
        invoiceNumber: '',
        expectedArrivalDate: ''
      }
    }
  },

  computed: {
    // 총 금액 자동 계산
    totalAmount() {
      if (this.formData.productQuantity && this.formData.unitPrice) {
        return this.formData.productQuantity * this.formData.unitPrice
      }
      return 0
    }
  },

  methods: {
    async handleSubmit() {
      try {
        // 폼 데이터에 총 금액 추가
        const orderData = {
          ...this.formData,
          totalPrice: this.totalAmount
        }
        
        await this.orderStore.createOrder(orderData)
        alert('발주가 성공적으로 등록되었습니다.')
        
        // 폼 초기화
        this.formData = {
              categoryName: '',
              productName: '',
              productQuantity: null,
              unitPrice: null,
              invoiceNumber: '',
              expectedArrivalDate: ''
        }
      } catch (error) {
        console.error('발주 등록 실패:', error)
        alert('발주 등록에 실패했습니다.')
      }
    }
  }
}
</script>