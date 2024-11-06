<!-- src/views/Order/OrderView.vue -->
<template>
    <!-- 메인 컨테이너: flex 클래스를 통해 요소들을 수평 배치 -->
    <div class="flex">
      <div class="flex-1 p-5 text-white">
        <!-- 주문 폼 제목 -->
        <h2 class="text-xl font-bold mb-4">User</h2>
  
        <!-- 전체 폼을 감싸는 컨테이너 -->
        <div class="flex bg-gray-950 text-white rounded-lg">
          <div class="flex-1 flex justify-center items-center p-5">
            <div class="bg-gray-950 p-8 rounded-lg w-2/3">
              
              <!-- 사용자 폼 구성 -->
              <form class="space-y-4">
                <!-- 이름 -->
                <div class="flex flex-col space-y-2">
                  <label class="text-lg font-bold text-gray-300">Name</label>
                  <!--데이터 가져오기-->
                  <input type="text" class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]" v-model="user.name" readonly />
                </div>
                
                <!-- ID -->
                <div class="flex flex-col space-y-2">
                  <label class="text-lg font-bold text-gray-300">ID</label>
                  <!-- 데이터 가져오기 -->
                  <input type="text" class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]" v-model="user.id" readonly />
                </div>
  
                <!-- 직급 -->
                <div class="flex flex-col space-y-2">
                  <label class="text-lg font-bold text-gray-300">직급</label>
                  <!-- 데이터 가져오기 -->
                  <input type="text" class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]" v-model="user.company" readonly />
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
import { ref } from 'vue';

export default {
  name: "UserView",
  setup() {
    const user = ref({
      name: '관리자_김포키',
      company: 'Administrator'
    });

    return {
      user,
    };
  }
};
</script>
