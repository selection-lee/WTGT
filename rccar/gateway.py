import asyncio
from bleak import BleakClient
import websockets
import struct  # struct 모듈을 이용하여 byte 데이터를 int로 변환
import json

# 각 ESP32의 BLE 주소와 UUID
ESP32_ADDRESS_1 = "3C:61:05:15:6F:EE"  # 첫 번째 ESP32의 BLE MAC 주소
ESP32_ADDRESS_2 = "8C:CE:4E:99:B3:F6"  # 두 번째 ESP32의 BLE MAC 주소
ESP32_ADDRESS_3 = "78:21:84:C7:0B:2A"  # 두 번째 ESP32의 BLE MAC 주소

# 각각 다른 특성 UUID
CHARACTERISTIC_UUID_1 = "beb5483e-36e1-4688-b7f5-ea07361b26a8"  # 첫 번째 ESP32 특성 UUID
CHARACTERISTIC_UUID_2 = "87654321-4321-4321-4321-0987654321ba"  # 두 번째 ESP32 특성 UUID
CHARACTERISTIC_UUID_3 = "f47ac10b-58cc-4372-a567-0e02b2c3d479"  # 세 번째 ESP32 특성 UUID

# EC2 서버 URL (웹소켓)
EC2_SERVER_URL = 'ws://k11c208.p.ssafy.io:8081/ws/agent'

# UUID를 ESP 이름으로 변환하는 딕셔너리
uuid_to_name = {
    "beb5483e-36e1-4688-b7f5-ea07361b26a8": "esp1",
    "87654321-4321-4321-4321-0987654321ba": "esp2",
    "f47ac10b-58cc-4372-a567-0e02b2c3d479": "esp3"
}

con_flag = False  # 기본값 설정
Cnt =0

# ESP32에서 받은 특성 값 처리 함수
async def notification_handler(sender, data: bytearray, websocket, car_number):
    global con_flag
    """
    이 함수는 ESP32로부터 받은 데이터를 처리하고 웹소켓으로 전송하는 콜백 함수입니다.
    """
    # UUID를 ESP 이름으로 변환
    sender_uuid = sender.uuid
    esp_name = uuid_to_name.get(str(sender_uuid))

    # bytearray 데이터를 uint32_t로 변환
    value = struct.unpack('<I', data)[0]  # little-endian 형식으로 uint32_t로 변환
    print(f"Received data from {esp_name}: {value}")

    # value가 2일 경우 con_flag를 True로 설정
    if value == 2:
        con_flag = True
        print("con_flag set to True is 2")
        # 컨베이어 벨트 esp32 데이터 전송
        print("컨베이어벨트 코드")
        # con_flag가 True일 때 [1]을 리스트로 전송
        asyncio.create_task(run(ESP32_ADDRESS_3, CHARACTERISTIC_UUID_3, [1], "esp3", websocket, 3))
        con_flag = False  # 전송 후 con_flag를 다시 False로 설정

    # value가 2일 경우 con_flag를 True로 설정
    # if value == 1:
    #     con_flag = True
    #     print("con_flag set to True is 1")
    #     # 컨베이어 벨트 esp32 데이터 전송
    #     # con_flag가 True일 때 [1]을 리스트로 전송
    #     asyncio.create_task(run(ESP32_ADDRESS_3, CHARACTERISTIC_UUID_3, [0], "esp3", websocket, 3))
    #     print("0 전송")
    #     con_flag = False  # 전송 후 con_flag를 다시 False로 설정

    # 수신한 데이터를 웹소켓으로 전송
    data_to_send = {
        "carNum": car_number,
        "isWhere": value
    }
    await websocket.send(json.dumps(data_to_send))
    print(f"Sent data to WebSocket: {data_to_send}")


# ESP32로 리스트 전송 함수
async def send_list(client: BleakClient, characteristic_uuid: str, data_list):
    """
    이 함수는 ESP32로 리스트 데이터를 전송하는 함수입니다.
    리스트 데이터를 bytearray로 변환하여 GATT 특성을 통해 전송합니다.
    """
    byte_data = struct.pack(f"<{len(data_list)}I", *data_list)  # 리스트를 uint32 형식으로 변환
    await client.write_gatt_char(characteristic_uuid, byte_data)
    print(f"Sent data to {characteristic_uuid}: {data_list}")


# ESP32와 연결하고 데이터를 수신하는 함수
async def run(address: str, characteristic_uuid: str, data_list, sender: str, websocket, car_number: int):
    async with BleakClient(address) as client:
        print(f"Connected to ESP32 at {address}")

        # 데이터 전송
        print(f"Sending data to {sender}: {data_list}")
        await send_list(client, characteristic_uuid, data_list)

        # 알림 수신 설정
        await client.start_notify(
            characteristic_uuid,
            lambda sender, received_data: asyncio.create_task(
                notification_handler(sender, received_data, websocket, car_number)
            )
        )
        print(f"Started notifications for {sender}")

        # 필요한 만큼 대기
        await asyncio.sleep(60)

# 각 car_number 별로 데이터를 저장할 딕셔너리
received_data = {1: [], 2: []}
async def websocket_handler():
    global Cnt
    async with websockets.connect(EC2_SERVER_URL) as websocket:
        print("Connected to the EC2 WebSocket server!")
        while True:
            message = await websocket.recv()
            data = json.loads(message)
            print(f"Received data from EC2: {data}")

            car_number = data.get("carNumber")
            goal = data.get("goal")
            route_list = data.get("route", [])
            ab_route_list = data.get("abRoute", [])

            if car_number is not None:
                # abRoute의 마지막 값 추출
                ab_route_last_value = ab_route_list[-1] if ab_route_list else 0

                # 데이터 리스트 생성
                data_list = route_list.copy()
                data_list.append(100)  # goal 마커
                data_list.append(goal)
                data_list.append(ab_route_last_value)

                print(f"Constructed data list: {data_list}")

                # car_number에 맞는 데이터 리스트에 추가
                received_data[car_number].append(data_list)

                # 데이터 3번 수신 후 처리
                if len(received_data[car_number]) == 3:
                    # 3번 받은 데이터를 하나로 합침
                    combined_data = [item for sublist in received_data[car_number] for item in sublist]
                    print(f"Combined data for car {car_number}: {combined_data}")

                    # ESP32로 전송
                    if car_number == 1:
                        asyncio.create_task(run(ESP32_ADDRESS_1, CHARACTERISTIC_UUID_1, combined_data, "esp1", websocket, 1))
                    elif car_number == 2:
                        asyncio.create_task(run(ESP32_ADDRESS_2, CHARACTERISTIC_UUID_2, combined_data, "esp2", websocket, 2))


                    # 데이터 초기화 (다시 3번 받기 위해)
                    received_data[car_number] = []
# main 함수
async def main():
    await websocket_handler()


# asyncio 이벤트 루프 실행
loop = asyncio.get_event_loop()
loop.run_until_complete(main())