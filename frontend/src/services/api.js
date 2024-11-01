// services/api.js
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  // timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 인증 관련 API
export const authApi = {
  // 로그인
  login: async (credentials) => {
    const response = await api.post('/auth/login', credentials)
    return response.data
  },
  
  // 토큰 갱신
  refresh: async (refreshToken) => {
    const response = await api.post('/auth/refresh', { refreshToken })
    return response.data
  }
}

// 요청 인터셉터
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 응답 인터셉터
// api.interceptors.response.use(
//   (response) => response.data, // 응답의 data 부분만 반환
//   (error) => {
  // 401 에러 발생 시 즉시 로그아웃 처리
//     if (error.response?.status === 401) {
//       // 인증 에러 처리 - 토큰만 삭제
//       localStorage.removeItem('token')
//       window.location.href = '/login'
//     }
//     return Promise.reject(error)
//   }
// )
// 응답 인터셉터 - 토큰 만료 처리
api.interceptors.response.use(
  (response) => response, // 응답 전체 객체 반환
  async (error) => {
    const originalRequest = error.config // 실패한 요청 정보 저장
    
    // 401 에러이고 아직 재시도하지 않은 요청인 경우
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true // 재시도 표시
      
      try {
        // 리프레시 토큰으로 새 액세스 토큰 발급 시도
        const refreshToken = localStorage.getItem('refreshToken')
        const response = await authApi.refresh(refreshToken)
        
        // 새 토큰 저장 및 헤더 업데이트
        localStorage.setItem('token', response.token)
        api.defaults.headers.Authorization = `Bearer ${response.token}`
        
        // 실패했던 요청 재시도
        return api(originalRequest)
      } catch (refreshError) {
        // 리프레시 토큰도 만료된 경우
        localStorage.removeItem('token')
        localStorage.removeItem('refreshToken')
        window.location.href = '/login'
        return Promise.reject(refreshError)
      }
    }
    
    return Promise.reject(error)
  }
)

export const dashboardApi = {
  getStatistics() {
    return api.get('/dashboard/statistics')
  },
  getLatestIncomes() {
    return api.get('/dashboard/incomes/latest')
  },
  getRecentUsers() {
    return api.get('/dashboard/users/recent')
  }
}

export default api