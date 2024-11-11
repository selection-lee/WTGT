<!-- src/views/Auth/SignUpView.vue -->
<template>
    <div class="h-screen flex overflow-y-auto">
      <div class="w-1/2 flex flex-col items-center bg-black p-8 sm:px-12 md:px-16">
        <div class="flex flex-col items-center mb-3">
          <img 
            src="@/assets/logo.png" 
            alt="WTGT Manager Logo" 
            class="w-16 h-auto mb-4"
          />
          <h2 class="text-[#FFB900] text-4xl font-bold italic">WTGT Manager</h2>
          <h1 class="text-white text-2xl font-bold mt-2">Sign Up</h1>
        </div>
      
        <!-- 회원가입 폼 --> 
        <div class="w-full max-w-md">
          <form @submit.prevent="handleSignUp" class="space-y-4">
            <!-- ID 입력 -->
            <div>
              <label class="block text-white text-xl mb-1">ID</label>
              <input 
                v-model="signUpForm.username"
                type="text"
                class="w-full p-3 rounded bg-[#1A1A1A] text-white border-none focus:outline-none"
              />
            </div>
            
            <!-- 닉네임 입력 -->
            <div>
              <label class="block text-white text-xl mb-1">닉네임</label>
              <input 
                v-model="signUpForm.nickname"
                type="text"
                class="w-full p-3 rounded bg-[#1A1A1A] text-white border-none focus:outline-none"
              />
            </div>
            
            <!-- PW 입력 -->
            <div>
              <label class="block text-white text-xl mb-1">비밀번호</label>
              <input 
                v-model="signUpForm.password"
                type="password"
                class="w-full p-3 rounded bg-[#1A1A1A] text-white border-none focus:outline-none"
              />
            </div>

            <!-- PW 확인 -->
            <div>
              <label class="block text-white text-xl mb-1">비밀번호 확인</label>
              <input 
                v-model="passwordConfirm"
                type="password"
                class="w-full p-3 rounded bg-[#1A1A1A] text-white border-none focus:outline-none"
              />
            </div>
            
            <!-- 에러 메시지 -->
            <div v-if="error" class="text-red-500 text-center">
              {{ error }}
            </div>

            <!-- 회원가입 버튼 -->
            <button 
              type="submit"
              class="w-full bg-[#FFB900] hover:bg-[#E5A700] text-black font-bold py-3 rounded text-lg"
            >
              회원가입
            </button>

            <!-- 로그인 페이지로 이동 -->
            <div class="text-center">
              <router-link 
                to="/login" 
                class="text-[#FFB900] hover:text-[#E5A700]"
              >
                이미 계정이 있으신가요? 로그인하기
              </router-link>
            </div>
          </form>
        </div>
      </div>
      
      <!-- 오른쪽 이미지 섹션 -->
      <div class="w-1/2 h-screen bg-[#808080]">
        <img 
          src="@/assets/loginIMG.png" 
          alt="Warehouse Management System" 
          class="w-full h-full object-cover"
        />
      </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()
const error = ref('')
const passwordConfirm = ref('')

const signUpForm = ref({
  username: '',
  nickname: '',
  password: ''
})

const handleSignUp = async () => {
  // 입력값 검증
  if (!signUpForm.value.username || !signUpForm.value.password || !signUpForm.value.nickname) {
    error.value = '모든 필드를 입력해주세요.'
    return
  }

  if (signUpForm.value.password !== passwordConfirm.value) {
    error.value = '비밀번호가 일치하지 않습니다.'
    return
  }

  try {
    await authStore.signup(signUpForm.value)
    router.push('/login')
  } catch (err) {
    if (err.response?.data?.code === 2001) {
      error.value = '이미 존재하는 사용자명입니다.'
    } else {
      error.value = '회원가입 중 오류가 발생했습니다.'
    }
    console.error('SignUp failed:', err)
  }
}
</script>