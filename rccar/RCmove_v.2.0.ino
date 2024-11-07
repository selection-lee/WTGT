/*

24/11/05
json 인코딩,디코딩
socket.io 송수신

*/

#include <SocketIOclient.h>
#include <WebSockets.h>
#include <WebSockets4WebServer.h>
#include <WebSocketsClient.h>
#include <WebSocketsServer.h>
#include <WebSocketsVersion.h>

#include <ArduinoJson.h>
#include <ArduinoWebsockets.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>


/*
=====================================
웹소켓 : 최단경로 들어오면 돌리기
=====================================
*/

//#include "move.ino"
//#include "changedir.ino"
//#include <WebSocketsClient.h>

#define IN1 3    // 왼쪽 앞바퀴
#define IN2 9     // 왼쪽 뒷바퀴
#define IN3 10   // 오른쪽 앞바퀴
#define IN4 11    // 오른쪽 뒷바퀴

#define IRL A3
#define IRR A4

#define ENA  5   // 왼쪽 모터 방향 제어 핀
#define ENB 6   // 오른쪽 모터 방향 제어 핀

WebSocketsClient webSocket;

bool executeLoop = false;
int routeArray[9] = { 0, 2, 1, 1, 2, 2, 0, 0, 1 };
int routelength = 9;
int robotheading = 1;
int speed = 100;
int nowCarNum = 0;
int nowGoal = 0;

const char* ssid = "SSAFY";
const char* password = "ssafy#1210";
const char* serverAddress = "ws://yourserver.com:port";


void setup() {
    Serial.begin(9600);

    // WiFi 연결
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
        delay(100);
    }
    Serial.println("WiFi Connected");

    // WebSocket 초기화 및 핸들러 설정
    webSocket.begin(serverAddress, 8080, "/socket.io/?EIO=4");
    webSocket.onEvent(socketIOEvent);

    // pin num
    pinMode(IN1, OUTPUT);  // 속도 제어 핀 설정
    pinMode(IN2, OUTPUT);
    pinMode(IN3, OUTPUT);
    pinMode(IN4, OUTPUT);

    pinMode(IRL, INPUT);
    pinMode(IRR, INPUT);

    rc_go();
}

void loop() {

    webSocket.loop();   // 웹소켓 연결, 수신 대기

    if (executeLoop) {
        Serial.print("json 수신 완료, loop 실행 중");
        executeLoop = false;

        // 좌표까지 이동
        for (int i = 0; i < routelength; i++) {
            Serial.print("현재 이동 좌표: ");
            Serial.println(routeArray[i]);

            if (robotheading != routeArray[i]) {
                Serial.println("방향전환중");
                changeDir(routeArray[i]);// 방향 전환
                continue;
            }

            Serial.println("linetracer");

            lineTracing(); // 앞으로(라인트레이서)

            // 최종 목적지 도착했을 경우
            if (i + 1 == routelength) {

                // 앞으로 이동
                rc_go();

                           
                // 도착 여부 통신 
                // 목적지 : 2
                sendWeb(2);


                // 180도 회전
                rc_turn180();
            }
        }
    }
}

// 직진
void rc_go() {
    Serial.println("직진!!!!!");
    digitalWrite(IN1, HIGH);
    digitalWrite(IN2, LOW);
    digitalWrite(IN3, HIGH);
    digitalWrite(IN4, LOW);
    analogWrite(ENA, 255);
    analogWrite(ENB, 255);
    delay(1000);
}

void rc_turn180() {
    digitalWrite(IN1, HIGH);
    digitalWrite(IN2, LOW);
    digitalWrite(IN3, LOW);
    digitalWrite(IN4, HIGH);
    analogWrite(ENA, 255);
    analogWrite(ENB, 255);
    delay(5000);

}

void rc_right() {
    Serial.println("살짝오른쪾으로!!!!");
    digitalWrite(IN1, LOW);
    digitalWrite(IN2, HIGH);
    digitalWrite(IN3, HIGH);
    digitalWrite(IN4, LOW);
    analogWrite(ENA, 255);
    analogWrite(ENB, 255);
    delay(500);

}

void rc_left() {
    Serial.println("살짝왼쪾으로!!!!");
    digitalWrite(IN1, HIGH);
    digitalWrite(IN2, LOW);
    digitalWrite(IN3, LOW);
    digitalWrite(IN4, HIGH);
    analogWrite(ENA, 255);
    analogWrite(ENB, 255);
    delay(500);

}

void rc_stop() {
    Serial.println("교차로 도착!!!!!!");
    analogWrite(ENA, 0);
    analogWrite(ENB, 0);
    digitalWrite(IN1, LOW);
    digitalWrite(IN2, LOW);
    digitalWrite(IN3, LOW);
    digitalWrite(IN4, LOW);
    delay(500);
}

// 데이터 수신
void socketIOEvent(socketIOmessageType_t type, uint8_t* payload, size_t length) {
    switch (type) {
    case sIOtype_DISCONNECT:
        USE_SERIAL.printf("[IOc] Disconnected!\n");
        break;
    case sIOtype_CONNECT:
        USE_SERIAL.printf("[IOc] Connected to url: %s\n", payload);

        // join default namespace (no auto join in Socket.IO V3)
        socketIO.send(sIOtype_CONNECT, "/");
        break;
    case sIOtype_EVENT:
        // 아두이노가 메세지를 수신하는 지점
        USE_SERIAL.printf("[IOc] get event: %s\n", payload);
        // 데이터 파싱
        parsingJson(payload);

        break;
    case sIOtype_ACK:
        USE_SERIAL.printf("[IOc] get ack: %u\n", length);
        hexdump(payload, length);
        break;
    case sIOtype_ERROR:
        USE_SERIAL.printf("[IOc] get error: %u\n", length);
        hexdump(payload, length);
        break;
    case sIOtype_BINARY_EVENT:
        USE_SERIAL.printf("[IOc] get binary: %u\n", length);
        hexdump(payload, length);
        break;
    case sIOtype_BINARY_ACK:
        USE_SERIAL.printf("[IOc] get binary ack: %u\n", length);
        hexdump(payload, length);
        break;
    }
}

// 웹으로 수신
void sendWeb(int tp) {
    // type에 따라서 수신 다르게 설정
    // json 데이터 만들기
    DynamicJsonDocument doc(1024);
    JsonObject sendjson = doc.to<JsonObject>();
    sendjson["carNumber"] = nowCarNum;
    sendjson["isWhere"] = tp;
    
    serializeJson(sendjson, Serial);    // 인코딩한 json 출력
    
    // 수신
    String jsonString;
    serializeJson(sendjson, jsonString);
    webSocket.sendEVENT(jsonString);  // 서버로 이벤트로 전송

    Serial.println("success sending");

}

// json 파싱
void parsingJson(uint8_t* payload, size_t length) {

    String message = String((char*)payload);

    // payload 아래처럼 구성했을 때 파싱 성공했음!
    // char pl[] = "{\"carNum\":\"22\",\"route\":[1, 2, 3, 1]}";  // 문자열 수정
    // parsingJson((uint8_t*)pl, strlen(pl));
    //

    // json 파싱
    JsonDocument jsonbuffer;
    DeserializationError error = deserializeJson(jsonbuffer, message);

    if (error) {
        Serial.print("json parsing Failed");
        Serial.println(error.c_str());
        return;
    }

    nowCarNum = jsonbuffer["carNumber"].as<int>();
    
    JsonArray bestroute = jsonbuffer["route"];
    routelength = bestroute.size();

    for (int i = 0; i < routelength; i++) {
        routeArray[i] = bestroute[i];
    }
    executeLoop = true;
}

// 라인트레이싱
void lineTracing() {
    // 예외처리.. 해야할 것 같은데... 굳게 믿어봐?

    while (1) {
        // action : L, R

        // 직진 : 미감지, 미감지
        if (digitalRead(IRL) == HIGH && digitalRead(IRR) == HIGH) {
            rc_go();
        }

        // 우회전 : 미감지, 감지
        // 왼바 : 앞으로, 오바: 뒤로
        else if (digitalRead(IRL) == HIGH && digitalRead(IRR) == LOW) {
            rc_right();
        }

        // 좌회전 : 감지, 미감지
        else if (digitalRead(IRL) == LOW && digitalRead(IRR) == HIGH) {
            rc_left();
        }

        // 교차로 : 감지, 감지
        else if (digitalRead(IRL) == LOW && digitalRead(IRR) == LOW) {
            rc_stop();
            // 교차로 : 1
            sendWeb(1);

            return;
        }
    }
}

// 방향 전환
void changeDir(int dir) {
    
    // 정지
    if (dir == 0) {
        digitalWrite(IN1, LOW);
        digitalWrite(IN2, LOW);
        digitalWrite(IN3, LOW);
        digitalWrite(IN4, LOW);
        analogWrite(ENA, 0);
        analogWrite(ENB, 0);
        delay(2000);
    }

    // 왼쪽 회전!
    else if (dir == 2) {
        digitalWrite(IN1, HIGH);
        digitalWrite(IN2, LOW);
        digitalWrite(IN3, LOW);
        digitalWrite(IN4, HIGH);
        analogWrite(ENA, 255);
        analogWrite(ENB, 255);
        delay(2000);
    }
    // 오른쪽 회전!
    else if (dir == 3) {
        digitalWrite(IN1, LOW);
        digitalWrite(IN2, HIGH);
        digitalWrite(IN3, HIGH);
        digitalWrite(IN4, LOW);
        analogWrite(ENA, 255);
        analogWrite(ENB, 255);
        delay(2000);
    }
    // 회전 후 정지
    digitalWrite(IN1, LOW);
    digitalWrite(IN2, LOW);
    digitalWrite(IN3, LOW);
    digitalWrite(IN4, LOW);
    analogWrite(ENA, 0);
    analogWrite(ENB, 0);
    delay(2000);
}
