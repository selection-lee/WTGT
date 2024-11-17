import { defineStore } from "pinia"
import api from "../services/api"

export const useStockStore = defineStore("stock", {
  state: () => ({
    products: [], // 백에서 받아온 상품 목록
    loading: false, // 데이터 로딩 상태 (api 요청 시작 시 true, 완료 시 false)
    error: null, // 에러 상태 (api 요청 실패 시 에러 정보 저장)
    lastUpdated: null, // 마지막 업데이트 시간
    autoUpdateInterval: null, // 자동 업데이트 인터벌 ID : setInterval의 id 저장 용도 - cleanup 시 clearInterval 호출하기 위함
    selectedDate: null, // 선택된 날짜 (날짜별 조회 시 사용)
  }),

  getters: {
    // 상태별 제품 수 계산
    statusCounts: (state) => {
      const counts = {
        LOADED: 0,
        IN_TRANSIT: 0,
        PENDING: 0,
        FAILED: 0,
      }

      state.products.forEach((product) => {
        if (counts.hasOwnProperty(product.productStatus)) {
          counts[product.productStatus]++
        }
      })

      return counts
    },

    // 전체 제품 수
    totalProducts: (state) => state.products.length,

    // 마지막 업데이트 시간을 포맷팅하여 반환
    formattedLastUpdate: (state) => {
      if (!state.lastUpdated) return "업데이트 정보 없음"
      return new Date(state.lastUpdated).toLocaleString("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
      })
    },
  },

  actions: {
    // 상품 목록 조회
    async fetchAllProducts() {
      // 이미 로딩 중이면 중복 요청 방지
      if (this.loading) return

      this.loading = true
      this.error = null

      try {
        const response = await api.get("/products")
        if (response.data.status === "SUCCESS") {
          this.products = response.data.data
          this.lastUpdated = new Date().toISOString()
        } else {
          throw new Error("Failed to fetch products")
        }
      } catch (err) {
        console.error("상품 목록 조회 실패:", err)
        this.error = err.message
      } finally {
        this.loading = false
      }
    },
    
    // 날짜별 상품 목록 조회
    async fetchProductsByDate(date) {
      if (this.loading) return

      this.loading = true
      this.error = null

      try {
        const response = await api.post("/products/date", {
          date: date,
        })

        if (response.data.status === "SUCCESS") {
          this.products = response.data.data
          this.selectedDate = date
          this.lastUpdated = new Date().toISOString()
        } else {
          throw new Error("Failed to fetch products")
        }
      } catch (err) {
        console.error("날짜별 상품 목록 조회 실패:", err)
        this.error = err.message
      } finally {
        this.loading = false
      }
    },

    // 현재 상태에 따른 데이터 조회
    async fetchProducts() {
      if (this.selectedDate) {
        await this.fetchProductsByDate(this.selectedDate)
      } else {
        await this.fetchAllProducts()
      }
    },

    // 날짜 선택 초기화 (전체 조회로 전환)
    clearDateSelection() {
      this.selectedDate = null
      this.fetchAllProducts()
    },

    // 자동 업데이트 시작
    startAutoUpdate(interval = 30000) {
      // 기본 30초
      this.stopAutoUpdate() // 기존 인터벌이 있다면 중지

      this.autoUpdateInterval = setInterval(() => {
        // setInterval: JS의 타이머 함수
        this.fetchProducts()
      }, interval)
    },

    // 자동 업데이트 중지
    stopAutoUpdate() {
      if (this.autoUpdateInterval) {
        clearInterval(this.autoUpdateInterval)
        this.autoUpdateInterval = null
      }
    },
  },
})
