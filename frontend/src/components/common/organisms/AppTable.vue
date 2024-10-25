<!-- components/common/organisms/AppTable.vue -->
<template>
  <div class="w-full whitespace-nowrap">
    <table class="w-full">
      <thead class="bg-black/60">
        <tr>
          <th
            v-for="header in headers"
            :key="header.value"
            :class="[
              'text-left py-3 px-2',
              header.class
            ]"
          >
            {{ header.text }}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="item in items"
          :key="getItemKey(item)"
          class="border-b border-gray-700"
        >
          <td
            v-for="header in headers"
            :key="header.value"
            class="py-3 px-2"
          >
            <slot
              :name="header.value"
              :item="item"
              :header="header"
            >
              {{ item[header.value] }}
            </slot>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
const props = defineProps({
  headers: {
    type: Array,
    required: true,
    validator: (headers) => headers.every(header => 
      typeof header.text === 'string' &&
      typeof header.value === 'string'
    )
  },
  items: {
    type: Array,
    required: true
  },
  itemKey: {
    type: String,
    default: 'id'
  }
})

const getItemKey = (item) => {
  return item[props.itemKey]
}
</script>