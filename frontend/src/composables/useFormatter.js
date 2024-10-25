// composables/useFormatter.js
import { ref } from 'vue'

export function useFormatter() {
  const currencyFormatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  })

  const formatCurrency = (value) => {
    return currencyFormatter.format(value)
  }

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return new Intl.DateTimeFormat('en-US', {
      day: 'numeric',
      month: 'short',
      year: 'numeric'
    }).format(date)
  }

  return {
    formatCurrency,
    formatDate
  }
}