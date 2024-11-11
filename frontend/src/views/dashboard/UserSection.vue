<!-- views/Dashboard/UserSection.vue -->
<template>
  <section class="mt-8">
    <h2 class="font-bold py-4 uppercase">Last 24h users</h2>
    <Card variant="dark" noPadding>
      <div class="overflow-x-scroll">
        <table class="w-full whitespace-nowrap">
          <thead>
            <tr>
              <th class="text-left py-3 px-2 rounded-l-lg">Name</th>
              <th class="text-left py-3 px-2">Email</th>
              <th class="text-left py-3 px-2">Group</th>
              <th class="text-left py-3 px-2">Status</th>
              <th class="text-left py-3 px-2 rounded-r-lg">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="user in users" 
              :key="user.email"
              class="border-b border-gray-700"
            >
              <td class="py-3 px-2 font-bold">
                <UserAvatar 
                  :image="user.image"
                  :name="user.name"
                  size="sm"
                />
              </td>
              <td class="py-3 px-2">{{ user.email }}</td>
              <td class="py-3 px-2">{{ user.group }}</td>
              <td class="py-3 px-2">
                <span :class="[
                  'px-2 py-1 rounded-full text-xs',
                  statusClasses[user.status]
                ]">
                  {{ user.status }}
                </span>
              </td>
              <td class="py-3 px-2">
                <div class="inline-flex items-center space-x-3">
                  <Button variant="outline" size="sm" title="Edit">
                    <Icons name="PencilIcon" size="sm" />
                  </Button>
                  <Button variant="outline" size="sm" title="Edit password">
                    <Icons name="KeyIcon" size="sm" />
                  </Button>
                  <Button variant="outline" size="sm" title="Suspend user">
                    <Icons name="NoSymbolIcon" size="sm" />
                  </Button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </Card>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import Card from '@/components/common/Card.vue'
import Button from '@/components/common/Button.vue'
import Icons from '@/components/common/Icons.vue'
import UserAvatar from '@/components/common/UserAvatar.vue'

const statusClasses = {
  'Approved': 'bg-green-500/20 text-green-500',
  'Pending': 'bg-yellow-500/20 text-yellow-500',
  'Suspended': 'bg-red-500/20 text-red-500'
}

const users = ref([
  {
    name: 'Thai Mei',
    email: 'thai.mei@abc.com',
    group: 'User',
    status: 'Approved',
    image: 'https://images.generated.photos/tGiLEDiAbS6NdHAXAjCfpKoW05x2nq70NGmxjxzT5aU/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/OTM4ODM1LmpwZw.jpg'
  },
  {
    name: 'Marquez Spineli',
    email: 'marquez.spineli@cba.com',
    group: 'User',
    status: 'Approved',
    image: 'https://images.generated.photos/tGiLEDiAbS6NdHAXAjCfpKoW05x2nq70NGmxjxzT5aU/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/OTM4ODM1LmpwZw.jpg'
  },
  {
    name: 'Mark Spike',
    email: 'mark.spike@abc.com',
    group: 'Administrator',
    status: 'Approved',
    image: 'https://images.generated.photos/niCVbIBAm4hahzwS83HoEtcVEIactkKohOzgXWYY4lM/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/NTk4ODczLmpwZw.jpg'
  }
])
</script>