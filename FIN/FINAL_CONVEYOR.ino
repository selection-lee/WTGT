#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include "Arduino.h"

// 모터 제어 핀 설정
#define IN1 32
#define IN2 33
#define IN3 25
#define IN4 26
#define ENA 23
#define ENB 22

BLEServer *pServer = NULL;
BLECharacteristic *pCharacteristic = NULL;
bool deviceConnected = false;
bool receivedFlag = false;
unsigned long lastPingTime = 0;

#define SERVICE_UUID "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID "f47ac10b-58cc-4372-a567-0e02b2c3d479"

// 연결 유지 신호 전송 함수
void sendPing() {
  if (deviceConnected) {
    pCharacteristic->setValue("ping");
    pCharacteristic->notify(); // 신호를 전송하여 연결 유지
    lastPingTime = millis();   // 마지막 신호 시간 업데이트
  }
}

void motorForward() {
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);
  analogWrite(ENA, 80); // PWM 속도 제어
  analogWrite(ENB, 80);
}

void motorStop() {
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, LOW);
}

class MyCallbacks : public BLECharacteristicCallbacks {
  void onWrite(BLECharacteristic *pCharacteristic) {
    String value = pCharacteristic->getValue();
    if (value.length() > 0) {
      int receivedValue = value[0];
      Serial.print("Received value: ");
      Serial.println(receivedValue);
      if (receivedValue == 1) {
        receivedFlag = true;
      } else if (receivedValue == 0) {
        Serial.println("Received signal: Stop motor");
        receivedFlag = false;
      }
    }
  }
};

class MyServerCallbacks : public BLEServerCallbacks {
  void onConnect(BLEServer* pServer) {
    Serial.println("Server connected!");
    deviceConnected = true;
    lastPingTime = millis(); // 연결 시점 초기화
  };

  void onDisconnect(BLEServer* pServer) {
    Serial.println("Server disconnected!");
    deviceConnected = false;
    delay(100);
    pServer->startAdvertising(); // 광고 재시작하여 재연결 가능하게 설정
  }
};

void setup() {
  Serial.begin(115200);

  // 모터 핀 설정
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);
  pinMode(IN3, OUTPUT);
  pinMode(IN4, OUTPUT);
  pinMode(ENA, OUTPUT);
  pinMode(ENB, OUTPUT);
  // ledcAttachChannel(ENA, 1000, 8, 2);
  // ledcAttachChannel(ENB, 1000, 8, 3);

  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, LOW);

  BLEDevice::init("ESP32 Device1");
  pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());

  BLEService *pService = pServer->createService(SERVICE_UUID);
  pCharacteristic = pService->createCharacteristic(
    CHARACTERISTIC_UUID,
    BLECharacteristic::PROPERTY_READ |
    BLECharacteristic::PROPERTY_WRITE |
    BLECharacteristic::PROPERTY_NOTIFY
  );
  pCharacteristic->addDescriptor(new BLE2902());
  pCharacteristic->setCallbacks(new MyCallbacks());

  pService->start();
  pServer->getAdvertising()->start();
  Serial.println("BLE 서버가 시작되었습니다.");
}

void loop() {
  if (deviceConnected && receivedFlag) {
    Serial.println("모터 전진: 0.8초");
    motorForward(); // 모터 전진
    delay(800);     // 0.8초간 전진

    Serial.println("모터 정지: 10초");
    motorStop();    // 모터 정지
    delay(10000);   // 10초 멈춤

    Serial.println("모터 전진: 3초");
    motorForward(); // 다시 모터 전진
    delay(3000);    // 3초간 전진

    Serial.println("루프 반복 시작");
    receivedFlag = false; // 동작 완료 후 플래그 초기화
  }

  // 주기적으로 연결 유지 신호를 전송하여 BLE 타임아웃 방지
  if (deviceConnected && (millis() - lastPingTime > 5000)) { // 5초마다 신호 전송
    sendPing();
  }
}