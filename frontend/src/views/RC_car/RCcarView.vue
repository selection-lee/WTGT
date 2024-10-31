<!-- src/views/RC_car/RCcarView.vue -->
<template>
    <div class="flex">
        <div class="flex-1 p-5 text-white">
            <h2 class="text-xl font-bold mb-4">RC car</h2>
            <div class="flex space-x-4">
                
                <!-- Bot Info (Bot List and Bot State together) -->
                <div class="flex flex-col space-y-4 w-1/3">
                    
                    <!-- Bot List -->
                    <div class="bg-gray-950 p-4 rounded-lg">
                        <h3 class="text-lg font-semibold mb-2">Bot List</h3>
                        <ul>
                            <li v-for="bot in botList" :key="bot.id" class="text-white-500 mb-1">
                                {{ bot.name }}: <span :class="bot.status === 'on' ? 'text-green-500' : 'text-red-500'">{{ bot.statusText }}</span>
                            </li>
                        </ul>
                    </div>
                    
                    <!-- Bot State -->
                    <div class="bg-gray-950 p-4 rounded-lg">
                        <h3 class="text-lg font-semibold mb-2">Bot State</h3>
                        <div v-for="bot in botList" :key="bot.id" class="mb-2">
                            <p class="text-yellow-500">{{ bot.name }}</p>
                            <p>State: {{ bot.state }}</p>
                            <p>Remaining Battery: {{ bot.battery }}%</p>
                        </div>
                    </div>
                </div>

                <!-- Map -->
                <div class="bg-gray-950 p-4 rounded-lg w-2/3">
                    <h3 class="text-lg font-semibold mb-2">MAP</h3>
                    <div class="relative w-full h-64">
                        <!--맵 수정필요-->
                        <div class="grid grid-cols-8 grid-rows-8" style="width: 400px; height: 400px;">
                            <div v-for="i in 64" :key="i" class="border border-white" style="width: 50px; height: 50px;"></div>
                        </div>
                        <div
                            v-for="bot in botList"
                            :key="bot.id"
                            :style="{ top: bot.position.y + 'px', left: bot.position.x + 'px' }"
                            class="absolute w-4 h-4 rounded-full"
                            :class="bot.color"
                        >
                            {{ bot.name }}
                        </div>
                        <div class="absolute top-5 left-1 bg-green-600 p-1 rounded text-xs">Conveyor Belt</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Sidebar from '../../components/layout/Sidebar.vue'

export default {
    components: {
        Sidebar,
    },
    data() {
        return {
            botList: [
                { id: 1, name: 'Bot 1', status: 'on', statusText: 'On the move', state: 'On the move', battery: 65, position: { x: 50, y: 50 }, color: 'bg-yellow-500' },
                { id: 2, name: 'Bot 2', status: 'on', statusText: 'On the move', state: 'On the move', battery: 45, position: { x: 200, y: 200 }, color: 'bg-red-500' },
            ]
        }
    }
}
</script>
