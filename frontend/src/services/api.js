// services/api.js
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 인증 관련 API
export const authApi = {
  // 로그인
  login: async (credentials) => {
    try{
      const formData = new URLSearchParams()
      
      formData.append('username', credentials.username)
      formData.append('password', credentials.password)

      const response = await api.post('/login', formData, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      })

      return response
    } catch (err) {
      console.error('API 로그인 에러: ', err)
      throw err
    }
  },
  
  // 회원가입
  signup: async (userData) => {
    const formData = new URLSearchParams()

    formData.append('username', userData.username)
    formData.append('password', userData.password)
    formData.append('nickname', userData.nickname)
    
    const response = await api.post('/signup', formData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
    return response.data
  }
}

// 요청 인터셉터 - jwt 토큰 처리
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('request 인터셉터 에러: ', error)
    return Promise.reject(error)
    // Promise.reject(error)
  }
)

// 응답 인터셉터 - 401 처리
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
  // 401 에러 발생 시 즉시 로그아웃 처리
    if (error.response?.status === 401) {
      // 인증 에러 처리 - 토큰만 삭제
      localStorage.removeItem('token')
      // 여기서는 /login 사용 - 프론트엔드 라우팅
      // 사용자에게 보여질 프론트엔드의 로그인 페이지 경로
      window.location.href = '/login'
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