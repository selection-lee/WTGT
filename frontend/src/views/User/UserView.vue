<!-- src/views/Order/OrderView.vue -->
<template>
  <!-- 메인 컨테이너: flex 클래스를 통해 요소들을 수평 배치 -->
  <div class="flex">
    <div class="flex-1 p-5 text-white">
      <!-- User 제목 -->
      <h2 class="text-xl font-bold mb-4">User</h2>

      <!-- 전체 폼을 감싸는 컨테이너 -->
      <div class="flex bg-gray-950 text-white rounded-lg">
        <div class="flex-1 flex justify-center items-center p-5">
          <div class="bg-gray-950 p-8 rounded-lg w-2/3">

            <!-- 사용자 폼 구성 -->
            <div class="space-y-4">
              <!-- Username -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">Username</label>
                <!--데이터 가져오기-->
                <input type="text"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  :value="authStore.user?.username" readonly />
              </div>

              <!-- Nickname -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">Nickname</label>
                <!-- 데이터 가져오기 -->
                <input type="text"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  :value="authStore.user?.nickname" readonly />
              </div>

              <!-- Role -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">Role</label>
                <!-- 데이터 가져오기 -->
                <input type="text"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  :value="authStore.user?.role" readonly />
              </div>

              <!-- Create At -->
              <div class="flex flex-col space-y-2">
                <label class="text-lg font-bold text-gray-300">가입 일자</label>
                <!-- 데이터 가져오기 -->
                <input type="text"
                  class="bg-gray-800 text-white p-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#FFB900]"
                  :value="formatDate(authStore.user?.createdAt)" readonly />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '../../stores/auth'

export default {
  setup() {
    const authStore = useAuthStore()

    // 날짜 포맷팅
    const formatDate = (dateString) => {
      if (!dateString) return 'N/A'
      return new Date(dateString).toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    }

    return { 
      authStore,
      formatDate
     }
  }
}
</script>
