#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include "Arduino.h"
#include <ESP32Servo.h>  // ESP32Servo 라이브러리 추가


// 핀 설정
#define IN1 16    // 왼쪽 앞, 뒷바퀴 모터 제어 (로봇입장)
#define IN2 17    // 왼쪽 앞, 뒷바퀴 모터 제어
#define IN3 18    // 오른쪽 앞, 뒷바퀴 모터 제어
#define IN4 19    // 오른쪽 앞, 뒷바퀴 모터 제어

#define IRL 14    // IR 센서 왼쪽 (로봇입장)
#define IRR 13    // IR 센서 오른쪽

#define ENA 4   // 왼쪽 모터 속도 제어 핀
#define ENB 5    // 오른쪽 모터 속도 제어 핀

// 서보 모터 핀 설정
#define SERVO_PIN_1 15

// 서보 객체 생성
Servo servo1;

bool executeLoop = false;
int routelength = 9;
int robotheading = 1;
int speed = 110;
int nowdir = 0;

int leftSensor = 0;
int rightSensor = 0;

int nowCarNum = 0;
int nowGoal = 0;
int dataSize = 0;

BLEServer *pServer = NULL;
BLECharacteristic *pCharacteristic = NULL;
bool deviceConnected = false;
bool listReceived = false;

// 리스트 선언
std::vector<uint32_t> receivedList;
size_t currentIndex = 0;

#define SERVICE_UUID "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID "beb5483e-36e1-4688-b7f5-ea07361b26a8" //1
//#define CHARACTERISTIC_UUID "87654321-4321-4321-4321-0987654321ba"  // 데이터 송수신에 사용할 UUID


// 수신된 데이터를 처리하는 함수 선언
void process_received_list(const uint8_t *data, size_t length);

// 서버 연결 상태를 추적하는 콜백
class MyServerCallbacks : public BLEServerCallbacks {
  void onConnect(BLEServer* pServer) {
    Serial.println("Server connected!");
    deviceConnected = true;
  };

  void onDisconnect(BLEServer* pServer) {
    Serial.println("Server disconnected!");
    deviceConnected = false;
    listReceived = false;  // 연결이 끊어지면 리스트 데이터 다시 수신하도록 초기화
    currentIndex =0;
    receivedList.clear();
  }
};

// 특성에 쓰기 콜백 설정하여 리스트 데이터 수신 처리
class MyCallbacks : public BLECharacteristicCallbacks {
  void onWrite(BLECharacteristic *pCharacteristic) {
    String value = pCharacteristic->getValue();  // std::string 대신 String 사용
    Serial.println("callback!!");
    Serial.println(value);  // std::string을 출력할 때는 c_str()을 사용
    if (value.length() > 0 && !listReceived) {
        process_received_list((uint8_t*)value.c_str(), value.length());
      listReceived = true;  // 리스트를 한번만 수신
    }
  }
};

// 수신된 데이터를 처리하는 함수 -> receivedList에 저장
void process_received_list(const uint8_t *data, size_t length) {
  Serial.println("Data received!");
  dataSize = length / sizeof(uint32_t);
  Serial.print("Received list data: ");
  
  for (int i = 0; i < dataSize; i++) {
    uint32_t value;
    memcpy(&value, &data[i * sizeof(uint32_t)], sizeof(uint32_t));
    receivedList.push_back(value);
    Serial.print(value);
    Serial.print(" ");
  }
  for (int i =0; i<dataSize ; i++){
    Serial.print(receivedList[i]);
  }
  Serial.println();
}

// 서버로 알림 전송
void send_value_from_list(int tp) {
  uint32_t value = tp;
  pCharacteristic->setValue((uint8_t*)&value, sizeof(value));
  pCharacteristic->notify();
  Serial.print("Sent value: ");
  Serial.println(value);
}


// 직진
void rc_go() {
  //Serial.println("직진!!");
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);
  analogWrite(ENA, 80);
  analogWrite(ENB, 80);
}

// 직진
void rc_back() {
  //Serial.println("직진!!");
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, HIGH);
  analogWrite(ENA, 75);
  analogWrite(ENB, 75);
}

void rc_turn180() {
  //Serial.println("TURN!!!");
  // delay(500);
      // 모터 속도 설정
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, HIGH);

  analogWrite(ENA, speed+7);
  analogWrite(ENB, speed+7);

  delay(150);
  while (true) {
    leftSensor = digitalRead(IRL);
    rightSensor = digitalRead(IRR);
    // leftSensor 감지 후 rightSensor가 LOW 상태를 감지
    if (rightSensor == LOW) {
      Serial.println("Right 검은 선 감지");
      delay(20);
      rc_stop();           // 모터 정지
      delay(5000);         // 안정화 대기
      break;
    }
  }
}

void rc_right() {
       // 오른쪽 모터는 기본 속도로 설정

  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, HIGH);

  //Serial.println("우회전!!");
  analogWrite(ENA, speed);  // 왼쪽 모터 속도를 조금 줄임
  analogWrite(ENB, speed);  
}

void rc_left() {

  digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);
  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);

      //Serial.println("우회전!!");
  analogWrite(ENA, speed);  // 왼쪽 모터 속도를 조금 줄임
  analogWrite(ENB, speed);  
}

void rc_stop() {
  //Serial.println("정지!!");
  //analogWrite(ENA, 0);
  //analogWrite(ENB, 0);

  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, LOW);

  analogWrite(ENA, 0);
  analogWrite(ENB, 0);
    
}

// 방향 전환
void changeDir(int direction) {
  // 방향에 따라 한쪽으로 회전
  if(direction == 0){
    // 정지
    rc_stop();
    delay(10);
    return;
  }

  else if (direction == 3) {
    Serial.println("change right!");
    rc_right();  // 우회전: 3
  } 
  else if(direction == 2) {
    Serial.println("change left!");
    rc_left();   // 좌회전:2
  }

  // 회전 중 IR 센서 확인
  while (true) {
    int leftSensor = digitalRead(IRL);
    int rightSensor = digitalRead(IRR);

    // 두 센서 중 하나가 검은 선을 감지하면 회전 종료
    if ((direction == 3 && rightSensor == LOW) || (direction == 2 && leftSensor == LOW)) {
        break;
    }
    //delay(10);  // 반복 간 딜레이 추가
  }

  rc_stop();  // 회전 후 정지
  delay(500);  // 안정화
}

// 라인트레이싱
void lineTracing() {
  //Serial.println("라인트레이싱중");

  while (true) {
    int leftSensor = digitalRead(IRL);
    int rightSensor = digitalRead(IRR);

    // 센서 값을 기준으로 동작 수행
    if (leftSensor == HIGH && rightSensor == HIGH) {
      rc_go();  // 직진
    } 
    else if (leftSensor == HIGH && rightSensor == LOW) {
      rc_right();  // 우회전
    } 
    else if (leftSensor == LOW && rightSensor == HIGH) {
      rc_left();  // 좌회전
    }
    // 교차로 : 감지, 감지
    else if (leftSensor == LOW && rightSensor == LOW) {
      rc_go();
      delay(250);
      rc_stop();
      delay(500);
      // 교차로 : 1
      // send_value_from_list(1);
      return;
    }
  }
}

void dropthebeat(){
  for (int angle = 0; angle <= 120; angle += 10) {
    servo1.write(angle);
    delay(10);  // 각도 이동 후 대기 시간
  }
  delay(500);

  for (int angle = 120; angle >= 0; angle -= 10) {
    servo1.write(angle);
    delay(10);  // 각도 이동 후 대기 시간
  }
}

void setup() {
  Serial.begin(115200);
  
  ledcAttachChannel(ENA, 1000, 8, 2);
  ledcAttachChannel(ENB, 1000, 8, 3);
  ledcAttachChannel(SERVO_PIN_1, 50, 10, 1);

  // pin 설정
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);
  pinMode(IN3, OUTPUT);
  pinMode(IN4, OUTPUT);
  pinMode(IRL, INPUT);
  pinMode(IRR, INPUT);
  // 서보 모터와 핀 연결
  servo1.attach(SERVO_PIN_1,1000,2400);

  for (int angle = 120; angle >= 0; angle -= 1) {
    servo1.write(angle);

    delay(20);  // 각도 이동 후 대기 시간
  }

  BLEDevice::init("ESP32 Device1");

  // BLE 서버 생성
  pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());

  // 서비스 및 특성 생성
  BLEService *pService = pServer->createService(SERVICE_UUID);

  // 데이터 송수신에 사용할 특성
  pCharacteristic = pService->createCharacteristic(
    CHARACTERISTIC_UUID,
    BLECharacteristic::PROPERTY_READ |
    BLECharacteristic::PROPERTY_WRITE |
    BLECharacteristic::PROPERTY_NOTIFY
  );

  // 알림을 활성화하는 디스크립터 추가
  pCharacteristic->addDescriptor(new BLE2902());  // BLE2902 디스크립터 추가
  // 알림 기능 활성화
  pCharacteristic->setNotifyProperty(true);  // 알림 설정

  pCharacteristic->setCallbacks(new MyCallbacks());

  pService->start();
  pServer->getAdvertising()->start();
  Serial.println("BLE 서버가 시작되었습니다.");

  Serial.println("Setup finished!");
    
}

void loop() {

  if (deviceConnected && listReceived) {

    Serial.println("START!");
    delay(2000);
    // 좌표까지 이동
    rc_go();

    while(true){
      int leftSensor = digitalRead(IRL);
      int rightSensor = digitalRead(IRR);
      //delay(50);

      if (leftSensor == LOW && rightSensor == LOW) {
        // delay(100);
        rc_stop();
        Serial.println("시작점도착!");
        delay(2000);
        break;
      }
    }

    Serial.print("for문 사이즈: ");
    Serial.println(dataSize);

    for (int i = 0; i < dataSize; i++) {
      Serial.print("now idx: ");
      Serial.println(i);    

      int nowSize = receivedList.size();
      if (i >= nowSize){
        continue;
      }
      for(int i =0; i<nowSize; i++){
        Serial.print(receivedList[i]);
        Serial.print(" ");
      }
      Serial.println();

      Serial.print("현재 이동 좌표: ");
      Serial.println(receivedList[i]);

      if (receivedList[i]==0){
        rc_stop();
        delay(1000);
        continue;
      }

      if (robotheading != receivedList[i] && receivedList[i]!=100) {
        Serial.println("방향전환중");
        changeDir(receivedList[i]);// 방향 전환
        continue;
      }

      // 100 : 도착정보가 온다 
      if (receivedList[i]==100){
        // 컨베이어 도착정보
        if (receivedList[i+1]==1){
          // 여기서 현재 방향을 받아온다
          Serial.println("컨베이어벨트 도착!");
          nowdir = receivedList[i+2];
          receivedList.erase(receivedList.begin() + (i + 2));
          receivedList.erase(receivedList.begin() + (i + 1));
          send_value_from_list(2);

          // 현재 방향이 0이면 왼쪽 회전
          if(nowdir ==3){
            Serial.println("왼쪽회전!");
            changeDir(2);
            delay(200);

            rc_go();
            delay(900);

            rc_stop();
            rc_turn180();
            delay(300);
            rc_back();
            delay(500);
            rc_stop();

            // 서보모터로 떨어트리기
            delay(10000);
            //lineTracing();
          }
          // 현재 방향이 2이면 오른쪽 회전
          else if(nowdir ==1){
            Serial.println("오른쪽회전!");
            changeDir(3);
            delay(200);
            rc_go();
            delay(900);
            rc_stop();
            rc_turn180();
            delay(300);
            rc_back();
            delay(500);
            rc_stop();

            delay(10000);
            //lineTracing();
          }
          // 현재 방향이 2이면
          else if(nowdir ==2){
            Serial.println("직진으로 들어간다!!!");
            rc_go();
            delay(900);
            rc_stop();
            rc_turn180();
            delay(300);
            rc_back();
            delay(500);
            rc_stop();

            delay(10000);
            //lineTracing();
          }
        }
        // 적재위치 도착정보
        else if(receivedList[i+1]==2){
          Serial.println("적재위치 도착!");
          nowdir = receivedList[i+2];
          receivedList.erase(receivedList.begin() + (i + 2));
          receivedList.erase(receivedList.begin() + (i + 1));

          // send_value_from_list(1);

          // 현재 방향이 0이면 오른쪽 회전
          if(nowdir ==3){
            Serial.println("오른쪽회전!");
            changeDir(3);
            delay(300);
            rc_go();
            delay(700);
            rc_stop();
            rc_turn180();

            // 서보모터로 떨어트리기
            dropthebeat();
            delay(3000);
            //lineTracing();
          }
          // 현재 방향이 2이면 왼쪽 회전
          else if(nowdir ==1){
            Serial.println("왼쪽회전!");
            changeDir(2);
            delay(300);
            rc_go();
            delay(700);
            rc_stop();
            rc_turn180();

            // 서보모터로 떨어트리기
            dropthebeat();
            delay(3000);
            //lineTracing();
          }
          else if(nowdir ==0){
            rc_go();
            delay(700);
            rc_stop();
            rc_turn180();

            // 서보모터로 떨어트리기
            dropthebeat();
            delay(3000);
            //lineTracing();
          }

        }
        // 충전소 도착정보
        else if(receivedList[i+1]==3){
          Serial.println("충전소 도착!");
          nowdir = receivedList[i+2];
          receivedList.erase(receivedList.begin() + (i + 2));
          receivedList.erase(receivedList.begin() + (i + 1));
          //send_value_from_list(1);

          // 현재 방향이 3이면 오른쪽 회전
          if(nowdir ==2){
            Serial.println("오른쪽회전!");
            changeDir(3);
            delay(200);
            rc_go();
            delay(500);
            rc_stop();
            rc_turn180();

            // 서보모터로 떨어트리기
            dropthebeat();
            delay(3000);
          }
          // 현재 방향이 1이면 왼쪽 회전
          else if(nowdir ==0){
            Serial.println("왼쪽회전!");
            changeDir(2);
            delay(200);
            rc_go();
            delay(500);
            rc_stop();
            rc_turn180();

            // 서보모터로 떨어트리기
            dropthebeat();
            delay(3000);
          }
          else if(nowdir ==3){
            rc_go();
            delay(500);
            rc_stop();
            rc_turn180();

            // 서보모터로 떨어트리기
            dropthebeat();
            delay(3000);
          }
        }
      }
      lineTracing(); // 라인트레이싱


      if (nowSize - 1 == i){
        Serial.println("작업완료");
        rc_stop();
        delay(2000);
      }
    }
  }
}
