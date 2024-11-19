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
                                {{ bot.name }}: <span
                                    :class="{
                                        'text-blue-500': bot.state === 'On the move',
                                        'text-yellow-500': bot.state === 'Loading Items',
                                        'text-green-500': bot.state === 'Charging',
                                        'text-purple-500': bot.state === 'Unloading'
                                    }">{{ bot.state
                                    }}</span>
                            </li>
                        </ul>
                    </div>

                    <!-- Bot State -->
                    <div class="bg-gray-950 p-4 rounded-lg">
                        <h3 class="text-lg font-semibold mb-2">Bot State</h3>
                        <div v-for="bot in botList" :key="bot.id" class="mb-2">
                            <p class="text-yellow-500">{{ bot.name }}</p>
                            <p>State: <span
                                    :class="{
                                        'text-blue-500': bot.state === 'On the move',
                                        'text-yellow-500': bot.state === 'Loading Items',
                                        'text-green-500': bot.state === 'Charging',
                                        'text-purple-500': bot.state === 'Unloading'
                                    }">{{ bot.state
                                    }}</span></p>
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
                            <div v-for="i in 8" :key="'v' + i" class="absolute bg-white" :style="{
                                left: (i * 30) + 'px',
                                top: 0,
                                width: '1px',
                                height: '270px'
                            }">
                            </div>

                            <!-- 가로 선 -->
                            <div v-for="i in 8" :key="'h' + i" class="absolute bg-white" :style="{
                                top: (i * 30) + 'px',
                                left: 0,
                                height: '1px',
                                width: '270px'
                            }">
                            </div>
                            <!-- RC 카 위치 표시 -->
                            <div v-for="bot in transformedBotList" :key="bot.id" :style="{
                                top: bot.position.y + 'px',
                                left: bot.position.x + 'px',
                                transform: 'translate(-8px, -8px)'  // w-4는 16px이므로 절반인 8px만큼 이동
                            }" class="absolute w-4 h-4 rounded-full flex items-center justify-center text-xs font-bold" :class="[bot.color, 'leading-none']">
                                <!-- {{ bot.id }} -->
                                <span class="translate-x-[-0.5px]">{{ bot.id }}</span>
                            </div>

                        </div>


                        <!-- 컨베이어 벨트 아이콘 -->
                        <div class="absolute" style="top: 260px; left: 138px;">
                            <svg width="24" height="32" viewBox="0 0 24 32" fill="none"
                                xmlns="http://www.w3.org/2000/svg">
                                <rect x="8" y="0" width="8" height="24" fill="#22c55e" />
                                <path d="M12 24L4 32L20 32L12 24Z" fill="#22c55e" />
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
            lastHouseArriveStates: {  // house_arrive의 이전 상태 저장
                1: false,
                2: false
            },
            lastPositions: {}, //마지막 위치 저장용
            // isWaitingNewPosition: {
            //     1: false,
            //     2: false
            // }, // 새로운 위치 저장용
            initialPositions: {
                1: {
                    x: 0.0,
                    y: -2.0,
                    z: -1.0
                },
                2: {
                    x: 1.0,
                    y: -2.0,
                    z: -1.0
                }
            }, // 초기 위치 저장
            // 새로운 경로 시작을 위한 플래그들
            resetComplete: {  // 초기화가 완전히 끝났는지 체크
                1: false,
                2: false
            },
            samePositionCount: {  // 같은 위치가 몇 번 들어왔는지 카운트
                1: 0,
                2: 0
            },
            // RC카 데이터를 각각 저장
            rcCarData: {
                1: {
                    rc_car_id: 1,
                    rc_car: {
                        x: 0.0,
                        y: -2.0,
                        z: -1.0
                    },
                    rc_car_angle: 1,
                    arrive: false,
                    convey_arrive: false,
                    house_arrive: false
                },
                2: {
                    rc_car_id: 2,
                    rc_car: {
                        x: 1.0,
                        y: -2.0,
                        z: -1.0
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
            ],
            webSocket: null,
            // 상태 변화 추적을 위한 플래그들 추가
            lastConveyArriveStates: {
                1: false,
                2: false
            },
            lastArriveStates: {
                1: false,
                2: false
            },
            stateChangeCount: {  // 상태 변화 후 같은 위치 카운트
                1: 0,
                2: 0
            },
            // 로딩/언로딩 상태가 끝났는지 체크
            loadingComplete: {
                1: false,
                2: false
            },
            unloadingComplete: {
                1: false,
                2: false
            }
        }
    },
    beforeDestroy() {
        if (this.webSocket) {
            this.webSocket.close();
            console.log('WebSocket manually disconnected');
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
                        x: carData.rc_car.z * 30 + 30, // 30 더해서 우측으로 한 칸 이동
                        y: (8 - carData.rc_car.x) * 30 // 8x8 그리드에 맞춰 7로 수정
                    }
                };
            });
        }
    },
    mounted() {
        if (!this.webSocket) {
            this.connectWebSocket();
        }
    },
    methods: {
        connectWebSocket() {
            this.webSocket = new WebSocket('ws://k11c208.p.ssafy.io:8081/ws/rccar');

            // 연결 성공 시
            this.webSocket.onopen = () => {
                console.log('WebSocket Connected!');
                // 웹소켓 연결 직후 각 RC카의 초기 위치를 설정
                Object.keys(this.rcCarData).forEach(id => {
                    this.rcCarData[id] = {
                        ...this.rcCarData[id],
                        rc_car: this.initialPositions[id]  // 초기 위치는 충전소로 설정
                    };

                    // botList에서 해당 bot의 상태를 'Charging'으로 변경
                    const botIndex = this.botList.findIndex(bot => bot.id === parseInt(id));
                    if (botIndex !== -1) {
                        this.botList[botIndex].state = 'Charging';
                    }
                });
            };

            this.webSocket.onmessage = (event) => {
                const data = JSON.parse(event.data);
                const id = data.rc_car_id;
                console.log('rccar:', id, '\ndata (x,y): ', data.rc_car.x, ',', data.rc_car.z, '\nHA:', data.house_arrive, '\nCA:', data.convey_arrive, '\nA:', data.arrive)
                
                const botIndex = this.botList.findIndex(bot => bot.id === id);
                
                // 최초 연결 시 받은 데이터일 경우 initialPositions 값을 사용
                if (!this.lastPositions[id]) {
                    this.rcCarData[id] = {
                        ...data,
                        rc_car: this.initialPositions[id]  // 초기 위치(충전소)로 덮어씌우기
                    };
                    if (botIndex !== -1) {
                        this.botList[botIndex].state = 'Charging';
                    }
                    this.lastPositions[id] = {...this.initialPositions[id]};
                    return;
                }

                // Bot 상태 업데이트
                if (botIndex === -1) return;

                // Bot 상태 업데이트 함수 수정
                const updateBotStatus = (newState) => {
                    if (botIndex !== -1) {
                        this.botList[botIndex].state = newState;
                        // this.botList[botIndex].statusText = newState;  // statusText도 함께 업데이트
                    }
                };

                // house_arrive가 true에서 false로 변경될 때
                if (this.lastHouseArriveStates[id] && !data.house_arrive) {
                    this.samePositionCount[id] = 1;
                    this.stateChangeCount[id] = 1;
                    this.rcCarData[id] = {
                        ...data,
                        rc_car: this.initialPositions[id]
                    };
                    // this.botList[botIndex].state = 'Charging';
                    updateBotStatus('Charging'); 
                }
                // house_arrive가 false이고 이전 위치와 같은 경우 (초기 위치 유지를 위한 처리)
                else if (!data.house_arrive && this.lastPositions[id] && 
                        data.rc_car.x === this.lastPositions[id].x && 
                        data.rc_car.z === this.lastPositions[id].z &&
                        this.samePositionCount[id] > 0) {
                    this.samePositionCount[id]++;
                    if (this.samePositionCount[id] >= 5) {
                        this.resetComplete[id] = true;
                    }
                    // 이 경우 rcCarData 업데이트 하지 않음 (초기 위치 유지)
                }
                // convey_arrive true로 변경될 때
                else if (!this.lastConveyArriveStates[id] && data.convey_arrive) {
                    this.stateChangeCount[id] = 0;
                    this.loadingComplete[id] = false;
                    this.rcCarData[id] = data;
                }
                // convey_arrive true에서 false로 변경될 때
                else if (this.lastConveyArriveStates[id] && !data.convey_arrive) {
                    this.stateChangeCount[id] = 1;
                    // this.botList[botIndex].state = 'Loading Items';
                    updateBotStatus('Loading Items'); 
                    this.rcCarData[id] = data;
                }
                // arrive true로 변경될 때
                else if (!this.lastArriveStates[id] && data.arrive) {
                    this.stateChangeCount[id] = 0;
                    this.unloadingComplete[id] = false;
                    this.rcCarData[id] = data;
                }
                // arrive true에서 false로 변경될 때
                else if (this.lastArriveStates[id] && !data.arrive) {
                    this.stateChangeCount[id] = 1;
                    // this.botList[botIndex].state = 'Unloading';
                    updateBotStatus('Unloading'); 
                    this.rcCarData[id] = data;
                }
                // 상태 변화 후 같은 위치가 계속 들어올 때
                // else if (this.lastPositions[id] && 
                //         data.rc_car.x === this.lastPositions[id].x && 
                //         data.rc_car.z === this.lastPositions[id].z) {
                //     // Charging 상태에서의 카운트
                //     if (this.samePositionCount[id] > 0) {
                //         this.samePositionCount[id]++;
                //         if (this.samePositionCount[id] >= 5) {
                //             this.resetComplete[id] = true;
                //         }
                //     }
                //     // Loading/Unloading 상태에서의 카운트
                //     if (this.stateChangeCount[id] > 0) {
                //         this.stateChangeCount[id]++;
                //         if (this.stateChangeCount[id] >= 5) {
                //             if (this.botList[botIndex].state === 'Loading Items') {
                //                 this.loadingComplete[id] = true;
                //                 this.botList[botIndex].state = 'On the move';
                //             } else if (this.botList[botIndex].state === 'Unloading') {
                //                 this.unloadingComplete[id] = true;
                //                 this.botList[botIndex].state = 'On the move';
                //             }
                //             this.stateChangeCount[id] = 0;
                //         }
                //         this.rcCarData[id] = data;
                //     }
                // }
                // Loading/Unloading 상태에서 같은 위치가 계속 들어올 때
                else if (this.stateChangeCount[id] > 0 && this.lastPositions[id] && 
                        data.rc_car.x === this.lastPositions[id].x && 
                        data.rc_car.z === this.lastPositions[id].z) {
                    this.stateChangeCount[id]++;
                    if (this.stateChangeCount[id] >= 5) {
                        if (this.botList[botIndex].state === 'Loading Items') {
                            this.loadingComplete[id] = true;
                            // this.botList[botIndex].state = 'On the move';
                            updateBotStatus('On the move'); 
                        } else if (this.botList[botIndex].state === 'Unloading') {
                            this.unloadingComplete[id] = true;
                            // this.botList[botIndex].state = 'On the move';
                            updateBotStatus('On the move'); 
                        }
                        this.stateChangeCount[id] = 0;
                    }
                    this.rcCarData[id] = data;
                }
                // 새로운 좌표가 들어올 때
                else if (!this.lastPositions[id] || 
                        data.rc_car.x !== this.lastPositions[id].x || 
                        data.rc_car.z !== this.lastPositions[id].z) {
                    // Charging에서 새로운 경로 시작
                    if (this.resetComplete[id]) {
                        this.resetComplete[id] = false;
                        this.samePositionCount[id] = 0;
                        // this.botList[botIndex].state = 'On the move';
                        updateBotStatus('On the move'); 
                    }
                    // Loading/Unloading 완료 후 새로운 좌표
                    else if (!data.convey_arrive && !data.arrive && 
                           (this.loadingComplete[id] || this.unloadingComplete[id])) {
                        // this.botList[botIndex].state = 'On the move';
                        updateBotStatus('On the move'); 
                    }
                    this.rcCarData[id] = data;
                }
                else {
                    this.rcCarData[id] = data;
                }

                // 현재 위치와 상태 저장
                this.lastPositions[id] = {...data.rc_car};
                this.lastHouseArriveStates[id] = data.house_arrive;
                this.lastConveyArriveStates[id] = data.convey_arrive;
                this.lastArriveStates[id] = data.arrive;


                // botList의 해당 봇 배터리 상태 업데이트
                // const botIndex = this.botList.findIndex(bot => bot.id === data.rc_car_id);
                if (data.battery_status !== undefined) {
                    this.botList[botIndex].battery = data.battery_status;
                }
            };

            this.webSocket.onerror = (error) => {
                console.error('WebSocket error:', error);
            };

            this.webSocket.onclose = () => {
                console.log('WebSocket connection closed');
                setTimeout(() => this.connectWebSocket(), 5000);
            };
        }
    }
}
</script>