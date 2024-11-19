// 상품 상태 enum (백엔드와 매칭)
export const ProductStatus = {
  PENDING_ARRIVAL: "PENDING_ARRIVAL", // 입고 대기 중
  PENDING_TRANSPORT: "PENDING_TRANSPORT", // 운반 대기 중
  IN_TRANSIT: "IN_TRANSIT", // 운반 중
  LOADED: "LOADED", // 적재 완료
}

// 카테고리 enum (백엔드와 매칭)
export const Category = {
  ELECTRONICS: "ELECTRONICS", // 전자제품
  FOOD: "FOOD", // 식품
  FURNITURE: "FURNITURE", // 가구
  CLOTHING: "CLOTHING", // 의류
  SPORTS: "SPORTS", // 스포츠용품
  BOOKS: "BOOKS", // 도서
  STATIONERY: "STATIONERY", // 문구류
  HOUSEHOLD: "HOUSEHOLD", // 생활용품
  COSMETICS: "COSMETICS", // 화장품
  AUTOMOTIVE: "AUTOMOTIVE", // 자동차용품
}

// 지역별 접두어 매핑 (A1, B2 등의 위치 표시를 위함)
export const areaPrefix = {
  SEOUL: "A",
  BUSAN: "B",
  DAEJEON: "C",
  GYEONGSANGBUK: "D",
  GWANGJU: "E",
  INCHEON: "F",
  DAEGU: "G",
  GYEONGGI: "H",
  GANGWON: "I",
  GYEONGSANGNAM: "J",
  ULSAN: "K",
  JEONLANAM: "L",
  JEONLABUK: "M",
  CHUNGCHEONGNAM: "N",
  CHUNGCHEONGBUK: "O",
  JEJU: "P",
}

// 위치 포맷팅 함수: 지역 코드와 위치 번호를 조합
export const formatLocation = (areaName, location) => {
  const prefix = areaPrefix[areaName] || "X" // 매핑되지 않은 지역은 'X' 사용
  // location이 null인 경우 빈 문자열 반환
  if (location === null) {
    return prefix
  }
  // productLocation이 있으면 prefix와 location 조합해서 반환
  return `${prefix}${location}`
}

// 필터 옵션 정의 (체크박스용)
export const filterOptions = [
  {
    value: ProductStatus.PENDING_ARRIVAL,
    label: "입고 대기 중",
    color: "slate", // Tailwind 색상 이름
  },
  {
    value: ProductStatus.PENDING_TRANSPORT,
    label: "운반 대기 중",
    color: "yellow",
  },
  {
    value: ProductStatus.IN_TRANSIT,
    label: "운반 중",
    color: "blue",
  },
  {
    value: ProductStatus.LOADED,
    label: "적재 완료",
    color: "green",
  },
]

// 카테고리 설명 텍스트
export const categoryDescriptions = {
  [Category.ELECTRONICS]: "전자제품",
  [Category.FOOD]: "식품",
  [Category.FURNITURE]: "가구",
  [Category.CLOTHING]: "의류",
  [Category.SPORTS]: "스포츠용품",
  [Category.BOOKS]: "도서",
  [Category.STATIONERY]: "문구류",
  [Category.HOUSEHOLD]: "생활용품",
  [Category.COSMETICS]: "화장품",
  [Category.AUTOMOTIVE]: "자동차용품",
}

// 카테고리 필터 옵션
export const categoryOptions = Object.entries(categoryDescriptions).map(
  ([value, label]) => ({
    value,
    label,
    color: "purple", // 카테고리는 모두 동일한 색상 사용
  })
)

// 상태별 표시 텍스트
export const statusDescriptions = {
  [ProductStatus.PENDING_ARRIVAL]: "입고 대기 중",
  [ProductStatus.PENDING_TRANSPORT]: "운반 대기 중",
  [ProductStatus.IN_TRANSIT]: "운반 중",
  [ProductStatus.LOADED]: "적재 완료",
}

// 상태별 스타일 정의 (배경색, 텍스트색, 프로그레스 바 색상)
export const statusStyles = {
  [ProductStatus.PENDING_ARRIVAL]: {
    bgColor: "bg-slate-500/20",
    textColor: "text-slate-400",
    barColor: "bg-slate-500",
  },
  [ProductStatus.PENDING_TRANSPORT]: {
    bgColor: "bg-orange-500/20",
    textColor: "text-orange-400",
    barColor: "bg-orange-500",
  },
  [ProductStatus.IN_TRANSIT]: {
    bgColor: "bg-blue-500/20",
    textColor: "text-blue-400",
    barColor: "bg-blue-500",
  },
  [ProductStatus.LOADED]: {
    bgColor: "bg-green-500/20",
    textColor: "text-green-400",
    barColor: "bg-green-500",
  },
}

// 다중 필터링 함수
// export const filterProducts = (products, selectedFilters) => {
//   // 필터가 없으면 전체 표시
//   if (selectedFilters.length === 0) return products

//   // 선택된 필터에 해당하는 상품만 반환
//   return products.filter((product) =>
//     selectedFilters.includes(product.productStatus)
//   )
// }
export const filterProducts = (
  products,
  selectedFilters,
  selectedCategories
) => {
  return products.filter((product) => {
    // 상태 필터: 선택된 필터가 없거나 상품의 상태가 선택된 필터에 포함
    const statusMatch =
      selectedFilters.length === 0 ||
      selectedFilters.includes(product.productStatus)

    // 카테고리 필터: 선택된 카테고리가 없거나 상품의 카테고리가 선택된 카테고리에 포함
    const categoryMatch =
      selectedCategories.length === 0 ||
      selectedCategories.includes(product.categoryName)

    // 두 조건 모두 만족해야 함 (AND 조건)
    return statusMatch && categoryMatch
  })
}

// 페이지네이션 함수
export const paginateProducts = (products, page, perPage) => {
  const start = (page - 1) * perPage
  const end = start + perPage
  return products.slice(start, end)
}

// 상태별 통계 계산
export const calculateStatusStats = (products) => {
  const counts = {
    pendingArrival: 0,
    pendingTransport: 0,
    inTransit: 0,
    loaded: 0,
  }
  // 각 상태별 건수 계산
  products.forEach((product) => {
    if (product.productStatus === ProductStatus.PENDING_ARRIVAL) {
      counts.pendingArrival++
    } else if (product.productStatus === ProductStatus.PENDING_TRANSPORT) {
      counts.pendingTransport++
    } else if (product.productStatus === ProductStatus.IN_TRANSIT) {
      counts.inTransit++
    } else if (product.productStatus === ProductStatus.LOADED) {
      counts.loaded++
    }
  })

  const total = products.length

  // 상태별 퍼센티지 계산
  return {
    counts,
    percentages: {
      pendingArrival: total
        ? ((counts.pendingArrival / total) * 100).toFixed(1)
        : "0.0",
      pendingTransport: total
        ? ((counts.pendingTransport / total) * 100).toFixed(1)
        : "0.0",
      inTransit: total ? ((counts.inTransit / total) * 100).toFixed(1) : "0.0",
      loaded: total ? ((counts.loaded / total) * 100).toFixed(1) : "0.0",
    },
    total,
  }
}

// 카테고리별 카운트 계산
export const calculateCategoryStats = (products) => {
  const counts = {}
  Object.keys(Category).forEach((category) => {
    counts[category] = products.filter(
      (p) => p.categoryName === category
    ).length
  })
  return counts
}

// 개별 상태 표시를 위한 스타일과 텍스트 반환 함수
export const getStatusDisplay = (status) => ({
  text: statusDescriptions[status] || status,
  ...statusStyles[status],
})
