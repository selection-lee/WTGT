// stores/order.js

import { defineStore } from 'pinia'
import { orderApi } from '@/services/api'

export const useOrderStore = defineStore('order', {
  state: () => ({
    // 카테고리 매핑 객체 추가
    categories: {
      ELECTRONICS: '전자제품',
      FOOD: '식품',
      FURNITURE: '가구',
      CLOTHING: '의류',
      SPORTS: '스포츠용품',
      BOOKS: '도서',
      STATIONERY: '문구류',
      HOUSEHOLD: '생활용품',
      COSMETICS: '화장품',
      AUTOMOTIVE: '자동차용품'
    }
  }),

  actions: {
    async createOrder(orderData) {
      try {
        const response = await orderApi.createOrder(orderData)
        return response
      } catch (error) {
        throw error
      }
    }
  }
})