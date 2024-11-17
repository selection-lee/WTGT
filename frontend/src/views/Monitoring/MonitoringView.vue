<template>
    <div class="flex">
        <!-- 메인 컨텐츠 -->
        <main class="flex-1 p-5 text-white bg-black">
            <!-- 헤더 -->
            <div class="flex justify-between items-center mb-4">
                <h2 class="text-xl font-bold">Stock Monitoring</h2>
                <div class="flex items-center gap-4">
                    <div class="flex items-center gap-2">
                        <!-- 날짜 선택 필터 -->
                        <input type="date" :value="stockStore.selectedDate" @change="handleDateChange"
                            class="bg-gray-700 text-white px-3 py-1 rounded" />
                        <!-- 전체 기간 조회 버튼 -->
                        <button @click="handleShowAll"
                            class="px-3 py-1 text-sm bg-gray-700 text-white rounded hover:bg-gray-600"
                            :class="{ 'bg-blue-500': !stockStore.selectedDate }">
                            전체 기간
                        </button>
                    </div>
                    <!-- 마지막 업데이트 시간 표시 -->
                    <span class="text-sm text-gray-400">
                        마지막 업데이트: {{ stockStore.formattedLastUpdate }}
                    </span>
                </div>
            </div>

            <!-- 현재 조회 중인 데이터 범위 표시 -->
            <div class="text-sm text-gray-400 mb-4">
                {{ stockStore.selectedDate ? `${stockStore.selectedDate} 입고 예정 물품` : '전체 기간 물품' }}
            </div>

            <!-- 입고 상품 테이블 섹션 -->
            <section class="mb-8">
                <div class="bg-gray-950 p-5 rounded-lg">
                    <!-- 필터 영역 -->
                    <div class="flex flex-col space-y-4 mb-4">
                        <div class="flex justify-between items-center">
                            <h3 class="text-xl font-semibold text-gray-300">입고 상품 목록</h3>

                            <!-- 필터 토글 버튼들 -->
                            <div class="space-x-2">
                                <button @click="toggleAllStatusFilters"
                                    class="px-3 py-1 rounded text-sm font-medium bg-gray-700 text-gray-300 hover:bg-gray-600">
                                    {{ selectedFilters.length === filterOptions.length ? '상태 전체 해제' : '상태 전체 선택' }}
                                </button>
                                <button @click="toggleAllCategoryFilters"
                                    class="px-3 py-1 rounded text-sm font-medium bg-gray-700 text-gray-300 hover:bg-gray-600">
                                    {{ selectedCategories.length === categoryOptions.length ? '카테고리 전체 해제' : '카테고리 전체 선택' }}
                                </button>
                            </div>
                        </div>

                        <!-- 상태 필터 -->
                        <div>
                            <p class="text-sm text-gray-400 mb-2">상태 필터</p>
                            <div class="flex flex-wrap gap-2">
                                <div v-for="option in filterOptions" :key="option.value" :class="{
                                    'flex items-center px-3 py-2 rounded cursor-pointer transition-colors': true,
                                    [`bg-${option.color}-500/20 hover:bg-${option.color}-500/30`]: isFilterSelected(option.value),
                                    'bg-gray-700 hover:bg-gray-600': !isFilterSelected(option.value)
                                }" @click="toggleFilter(option.value)">
                                    <div class="flex items-center space-x-2">
                                        <!-- 체크박스 -->
                                        <div :class="{
                                            'w-4 h-4 rounded border transition-colors': true,
                                            [`border-${option.color}-400 bg-${option.color}-400`]: isFilterSelected(option.value),
                                            'border-gray-400': !isFilterSelected(option.value)
                                        }">
                                            <svg v-if="isFilterSelected(option.value)" class="w-4 h-4 text-white"
                                                viewBox="0 0 20 20" fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                        </div>
                                        <!-- 라벨과 카운트 -->
                                        <span :class="{
                                            'text-sm font-medium': true,
                                            [`text-${option.color}-400`]: isFilterSelected(option.value),
                                            'text-gray-400': !isFilterSelected(option.value)
                                        }">
                                            {{ option.label }}
                                            <span class="ml-1 opacity-75">
                                                ({{ getStatusCount(option.value) }})
                                            </span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 카테고리 필터 -->
                        <div>
                            <p class="text-sm text-gray-400 mb-2">카테고리 필터</p>
                            <div class="flex flex-wrap gap-2">
                                <div v-for="option in categoryOptions" :key="option.value" :class="{
                                    'flex items-center px-3 py-2 rounded cursor-pointer transition-colors': true,
                                    'bg-purple-500/20 hover:bg-purple-500/30': isCategorySelected(option.value),
                                    'bg-gray-700 hover:bg-gray-600': !isCategorySelected(option.value)
                                }" @click="toggleCategory(option.value)">
                                    <div class="flex items-center space-x-2">
                                        <div :class="{
                                            'w-4 h-4 rounded border transition-colors': true,
                                            'border-purple-400 bg-purple-400': isCategorySelected(option.value),
                                            'border-gray-400': !isCategorySelected(option.value)
                                        }">
                                            <svg v-if="isCategorySelected(option.value)" class="w-4 h-4 text-white"
                                                viewBox="0 0 20 20" fill="currentColor">
                                                <path fill-rule="evenodd"
                                                    d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                                                    clip-rule="evenodd" />
                                            </svg>
                                        </div>
                                        <span :class="{
                                            'text-sm font-medium': true,
                                            'text-purple-400': isCategorySelected(option.value),
                                            'text-gray-400': !isCategorySelected(option.value)
                                        }">
                                            {{ option.label }}
                                            <span class="ml-1 opacity-75">
                                                ({{ getCategoryCount(option.value) }})
                                            </span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 로딩 상태 -->
                    <div v-if="stockStore.loading" class="text-center py-4 text-gray-400">
                        데이터를 불러오는 중...
                    </div>

                    <!-- 에러 상태 -->
                    <div v-else-if="stockStore.error" class="text-red-400 p-4">
                        {{ stockStore.error }}
                    </div>

                    <!-- 필터링 결과가 없을 때 -->
                    <div v-else-if="filteredProducts.length === 0" class="text-center py-4 text-gray-400">
                        선택한 조건에 해당하는 상품이 없습니다
                    </div>

                    <!-- 테이블과 페이지네이션 -->
                    <template v-else>
                        <table class="w-full bg-gray-700 rounded-lg text-center">
                            <thead>
                                <tr class="bg-gray-800">
                                    <th class="py-2">ID</th>
                                    <th>상품</th>
                                    <th>수량</th>
                                    <th>상태</th>
                                    <th>적재 위치</th>
                                    <th>Bot</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="product in paginatedProducts" :key="product.productId"
                                    class="border-t border-gray-500">
                                    <td class="py-2">{{ product.productId }}</td>
                                    <td>{{ product.productName }}</td>
                                    <td>{{ product.quantity }}</td>
                                    <td>
                                        <span :class="{
                                            'px-2 py-1 rounded text-xs font-medium': true,
                                            [getStatusDisplay(product.productStatus).bgColor]: true,
                                            [getStatusDisplay(product.productStatus).textColor]: true
                                        }">
                                            {{ getStatusDisplay(product.productStatus).text }}
                                        </span>
                                    </td>
                                    <td>{{ formatLocation(product.areaName, product.productLocation) }}</td>
                                    <td>{{ product.bot || '-' }}</td>
                                </tr>
                            </tbody>
                        </table>

                        <!-- 페이지네이션 -->
                        <div class="mt-4 flex justify-between items-center">
                            <div class="text-sm text-gray-400">
                                필터링된 {{ filteredProducts.length }}건 중
                                {{ (currentPage - 1) * itemsPerPage + 1 }}-
                                {{ Math.min(currentPage * itemsPerPage, filteredProducts.length) }}건
                            </div>
                            <div class="flex space-x-2">
                                <button @click="currentPage = page" v-for="page in totalPages" :key="page"
                                    :disabled="currentPage === page" :class="{
                                        'px-3 py-1 rounded': true,
                                        'bg-blue-500 text-white': currentPage === page,
                                        'bg-gray-700 text-gray-400 hover:bg-gray-600': currentPage !== page
                                    }">
                                    {{ page }}
                                </button>
                            </div>
                        </div>
                    </template>
                </div>
            </section>

            <!-- 입고 단계별 건수 -->
            <section class="bg-gray-950 p-5 rounded-lg">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-xl font-semibold text-gray-300">입고 단계별 건수</h3>
                    <!-- 업데이트 버튼들 -->
                    <div class="space-x-2">
                        <button @click="toggleAutoUpdate" :class="{
                            'px-3 py-1 rounded text-sm font-medium': true,
                            'bg-green-500/20 text-green-400': isAutoUpdateEnabled,
                            'bg-gray-500/20 text-gray-400': !isAutoUpdateEnabled
                        }">
                            {{ isAutoUpdateEnabled ? '자동 업데이트 중지' : '자동 업데이트 시작' }}
                        </button>
                        <button @click="stockStore.fetchProducts"
                            class="px-3 py-1 rounded text-sm font-medium bg-blue-500/20 text-blue-400"
                            :disabled="stockStore.loading">
                            수동 업데이트
                        </button>
                    </div>
                </div>

                <table class="w-full bg-gray-700 rounded-lg text-center table-fixed">
                    <thead>
                        <tr class="bg-gray-800">
                            <th class="py-2 w-1/4">입고 예정</th>
                            <th class="py-2 w-1/4">운반 대기</th>
                            <th class="py-2 w-1/4">운반 중</th>
                            <th class="py-2 w-1/4">적재 완료</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="py-2 text-xl font-bold">
                                {{ statusStats.counts.pendingArrival }}
                                <p class="text-sm text-slate-400 mt-1">
                                    {{ statusStats.percentages.pendingArrival }}%
                                </p>
                            </td>
                            <td class="py-2 text-xl font-bold">
                                {{ statusStats.counts.pendingTransport }}
                                <p class="text-sm text-orange-400 mt-1">
                                    {{ statusStats.percentages.pendingTransport }}%
                                </p>
                            </td>
                            <td class="py-2 text-xl font-bold">
                                {{ statusStats.counts.inTransit }}
                                <p class="text-sm text-blue-400 mt-1">
                                    {{ statusStats.percentages.inTransit }}%
                                </p>
                            </td>
                            <td class="py-2 text-xl font-bold">
                                {{ statusStats.counts.loaded }}
                                <p class="text-sm text-green-400 mt-1">
                                    {{ statusStats.percentages.loaded }}%
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" class="py-2">
                                <!-- 진행률 바 -->
                                <div class="relative w-full bg-gray-600 rounded h-4 mt-2 flex">
                                    <div class="h-4" :class="statusStyles[ProductStatus.PENDING_ARRIVAL].barColor"
                                        :style="{ width: statusStats.percentages.pendingArrival + '%' }"></div>
                                    <div class="h-4" :class="statusStyles[ProductStatus.PENDING_TRANSPORT].barColor"
                                        :style="{ width: statusStats.percentages.pendingTransport + '%' }"></div>
                                    <div class="h-4" :class="statusStyles[ProductStatus.IN_TRANSIT].barColor"
                                        :style="{ width: statusStats.percentages.inTransit + '%' }"></div>
                                    <div class="h-4" :class="statusStyles[ProductStatus.LOADED].barColor"
                                        :style="{ width: statusStats.percentages.loaded + '%' }"></div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </section>
        </main>
    </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { useStockStore } from '@/stores/stock'
import {
    ProductStatus,
    statusStyles,
    filterOptions,
    categoryOptions,
    filterProducts,
    paginateProducts,
    calculateStatusStats,
    getStatusDisplay,
    formatLocation
} from './monitoringUtils'

// Store 초기화
const stockStore = useStockStore()

// 상태 관리
const selectedFilters = ref([])        // 선택된 상태 필터
const selectedCategories = ref([])     // 선택된 카테고리 필터
const currentPage = ref(1)             // 현재 페이지
const itemsPerPage = 10                // 페이지당 항목 수
const isAutoUpdateEnabled = ref(false) // 자동 업데이트 상태

// 날짜 선택 핸들러
const handleDateChange = (event) => {
    const date = event.target.value
    if (date) {
        stockStore.fetchProductsByDate(date)
    }
}

// 전체 기간 조회 핸들러
const handleShowAll = () => {
    stockStore.clearDateSelection()
}

// 필터 관련 함수들
const toggleFilter = (status) => {
    const index = selectedFilters.value.indexOf(status)
    if (index === -1) {
        selectedFilters.value.push(status)
    } else {
        selectedFilters.value.splice(index, 1)
    }
}

// 상태 전체 선택/해제 토글
const toggleAllStatusFilters = () => {
    if (selectedFilters.value.length === filterOptions.length) {
        selectedFilters.value = []
    } else {
        selectedFilters.value = filterOptions.map(option => option.value)
    }
}

// 상태 필터 선택 여부 확인
const isFilterSelected = (status) => {
    return selectedFilters.value.includes(status)
}

// 상태별 카운트 계산
const getStatusCount = (status) => {
    return stockStore.products.filter(p => p.productStatus === status).length
}

// 카테고리 필터 토글
const toggleCategory = (category) => {
    const index = selectedCategories.value.indexOf(category)
    if (index === -1) {
        selectedCategories.value.push(category)
    } else {
        selectedCategories.value.splice(index, 1)
    }
}

// 카테고리 전체 선택/해제 토글
const toggleAllCategoryFilters = () => {
    if (selectedCategories.value.length === categoryOptions.length) {
        selectedCategories.value = []
    } else {
        selectedCategories.value = categoryOptions.map(option => option.value)
    }
}

// 카테고리 선택 여부 확인
const isCategorySelected = (category) => {
    return selectedCategories.value.includes(category)
}

// 카테고리별 카운트 계산
const getCategoryCount = (category) => {
    return stockStore.products.filter(p => p.categoryName === category).length
}

// 필터링된 제품 목록 (상태와 카테고리 필터 모두 적용)
const filteredProducts = computed(() =>
    filterProducts(stockStore.products, selectedFilters.value, selectedCategories.value)
)

// 전체 페이지 수 계산
const totalPages = computed(() =>
    Math.ceil(filteredProducts.value.length / itemsPerPage)
)

// 현재 페이지의 제품 목록
const paginatedProducts = computed(() =>
    paginateProducts(filteredProducts.value, currentPage.value, itemsPerPage)
)

// 상태별 통계
const statusStats = computed(() =>
    calculateStatusStats(stockStore.products)
)

// 자동 업데이트 토글
const toggleAutoUpdate = () => {
    isAutoUpdateEnabled.value = !isAutoUpdateEnabled.value
    if (isAutoUpdateEnabled.value) {
        stockStore.startAutoUpdate(30000) // 30초마다 갱신
    } else {
        stockStore.stopAutoUpdate()
    }
}

// 필터 변경 시 페이지 1로 리셋
watch([selectedFilters, selectedCategories], () => {
    currentPage.value = 1
})

// 컴포넌트 마운트 시 전체 데이터로 시작
onMounted(() => {
    stockStore.fetchAllProducts()
})

// 컴포넌트 언마운트 시 자동 업데이트 정리
onUnmounted(() => {
    stockStore.stopAutoUpdate()
})
</script>