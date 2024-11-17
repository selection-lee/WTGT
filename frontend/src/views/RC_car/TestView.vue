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
                    <div class="relative w-full h-80">
                        <!--맵-->
                        <div class="relative" style="width: 270px; height: 270px; left: 90px;">
                            <!-- 세로 선 -->
                            <div v-for="i in 8" :key="'v'+i" 
                                class="absolute bg-white"
                                :style="{ 
                                    left: (i * 30) + 'px', 
                                    top: 0, 
                                    width: '1px', 
                                    height: '270px'
                                }">
                            </div>

                            <!-- 가로 선 -->
                            <div v-for="i in 8" :key="'h'+i" 
                                class="absolute bg-white"
                                :style="{ 
                                    top: (i * 30) + 'px', 
                                    left: 0, 
                                    height: '1px', 
                                    width: '270px'
                                }">
                            </div>
                        </div>

                        <!-- RC 카 위치 표시 -->
                        <div
                            v-for="bot in transformedBotList"
                            :key="bot.id"
                            :style="{ 
                                top: bot.position.y + 'px', 
                                left: (bot.position.x + 90) + 'px'
                            }"
                            class="absolute w-4 h-4 rounded-full"
                            :class="bot.color"
                        >
                            {{ bot.name }}
                        </div>
                        <!-- 컨베이어 벨트 아이콘 -->
                        <div class="absolute" style="top: 260px; left: 138px;">
                            <svg 
                                width="24" 
                                height="32" 
                                viewBox="0 0 24 32" 
                                fill="none" 
                                xmlns="http://www.w3.org/2000/svg"
                            >
                                <rect x="8" y="0" width="8" height="24" fill="#22c55e" />
                                <path 
                                    d="M12 24L4 32L20 32L12 24Z" 
                                    fill="#22c55e"
                                />
                                <rect x="4" y="4" width="16" height="2" fill="#1a8a3d" />
                                <rect x="4" y="12" width="16" height="2" fill="#1a8a3d" />
                                <rect x="4" y="20" width="16" height="2" fill="#1a8a3d" />
                            </svg>
                        </div>
                        <!-- 설명 텍스트 -->
                        <div class="absolute top-[295px] left-[115px] text-xs">
                            Conveyor
                        </div>
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
            // RC카 데이터를 각각 저장
            rcCarData: {
                1: {
                    rc_car_id: 1,
                    rc_car: {
                        x: 0.0,
                        y: -2.0,
                        z: 0.0
                    },
                    rc_car_angle: 1,
                    arrive: false,
                    convey_arrive: false,
                    house_arrive: false
                },
                2: {
                    rc_car_id: 2,
                    rc_car: {
                        x: 0.0,
                        y: -2.0,
                        z: 0.0
                    },
                    rc_car_angle: 1,
                    arrive: false,
                    convey_arrive: false,
                    house_arrive: false
                }
            },
            botList: [
                { 
                    id: 1, 
                    name: 'Bot 1', 
                    status: 'on', 
                    statusText: 'On the move', 
                    state: 'On the move', 
                    battery: 65, 
                    color: 'bg-yellow-500' 
                },
                { 
                    id: 2, 
                    name: 'Bot 2', 
                    status: 'on', 
                    statusText: 'On the move', 
                    state: 'On the move', 
                    battery: 45, 
                    color: 'bg-red-500' 
                }
            ]
        }
    },
    computed: {
        transformedBotList() {
            return this.botList.map(bot => {
                // 각 봇의 ID에 해당하는 RC카 데이터 사용
                const carData = this.rcCarData[bot.id]
                return {
                    ...bot,
                    position: {
                        x: carData.rc_car.x * 30,
                        y: (7 - carData.rc_car.y) * 30 // 8x8 그리드에 맞춰 7로 수정
                    }
                };
            });
        }
    },
    mounted() {
        this.connectWebSocket();
    },
    methods: {
        connectWebSocket() {
            const ws = new WebSocket('ws://k11c208.p.ssafy.io:8081/ws/rccar');
            
            // 연결 성공 시
            ws.onopen = () => {
                console.log('WebSocket Connected!');
            };

            ws.onmessage = (event) => {
                const data = JSON.parse(event.data);
                // RC카 ID에 따라 해당하는 데이터 업데이트
                console.log('rccar data 웹소켓: ',data)
                this.rcCarData[data.rc_car_id] = data;
                // if (data.convey_arrive==true or ...) {
                //     count ++
                //     if count === 5 {
                //         // status 를 on the move 

                //     }
                // }
            };
            
            ws.onerror = (error) => {
                console.error('WebSocket error:', error);
            };
            
            ws.onclose = () => {
                console.log('WebSocket connection closed');
                setTimeout(() => this.connectWebSocket(), 5000);
            };
        }
    }
}
</script>