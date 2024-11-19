<!-- src/views/Auth/LoginView.vue -->

<template>
  <!-- overflow-hidden으로 스크롤 제거, h-screen으로 화면 높이에 정확히 맞춤 -->
  <div class="h-screen flex overflow-hidden">
    <!-- 왼쪽 로그인 섹션 -->
    <div class="w-1/2 flex flex-col items-center bg-black p-16">
      <!-- 로고와 텍스트를 감싸는 중앙 정렬 컨테이너 -->
      <div class="flex flex-col items-center mb-5">
        <!-- 로고: 너비만 고정하고 높이는 자동 조정 -->
        <img src="@/assets/logo.png" alt="WTGT Manager Logo" class="w-24 h-auto mb-4" />

        <!-- WTGT Manager 텍스트 -->
        <h2 class="text-[#FFB900] text-5xl font-bold italic">WTGT Manager</h2>

        <!-- Log In 텍스트 -->
        <h1 class="text-white text-3xl font-bold my-4">Log In</h1>
      </div>

      <!-- 로그인 폼 -->
      <div class="w-full max-w-md">
        <form @submit.prevent="handleLogin" class="space-y-8">
          <!-- ID 입력 -->
          <div>
            <label class="block text-white text-xl mb-3">ID</label>
            <input v-model="loginForm.username" type="text"
              class="w-full p-4 rounded bg-[#1A1A1A] text-white border-none focus:outline-none" />
          </div>

          <!-- PW 입력 -->
          <div>
            <label class="block text-white text-xl mb-3">PW</label>
            <input v-model="loginForm.password" type="password"
              class="w-full p-4 rounded bg-[#1A1A1A] text-white border-none focus:outline-none" />
          </div>

          <!-- 로그인 버튼 -->
          <button type="submit"
            class="w-full bg-[#FFB900] hover:bg-[#E5A700] text-black font-bold py-4 px-4 rounded text-lg mt-8">
            로그인
          </button>
        </form>
      </div>
      
      <!-- 회원가입 -->
      <div class="text-center mt-4">
        <router-link to="/signup" class="text-[#FFB900] hover:text-[#E5A700]">
          계정이 없으신가요? 회원가입하기
        </router-link>
      </div>
    </div>

    <!-- 오른쪽 이미지 섹션 / 부모 컨테이너 전체 높이 차지하도록 h-screen c추가-->
    <div class="w-1/2 h-screen bg-[#808080]"> <!-- 회색 배경 추가 -->
      <img src="@/assets/loginIMG.png" alt="Warehouse Management System" class="w-full h-full object-cover" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()

// 폼 데이터
const loginForm = ref({
  username: '',
  password: ''
})

// 로그인 핸들러
const handleLogin = async () => {
  try {
    await authStore.login(loginForm.value)
    router.push('/') // 로그인 성공 시 대시보드로 이동
  } catch (error) {
    console.error('Login failed:', error)
  }
}
</script>