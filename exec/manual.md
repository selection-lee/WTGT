# 와따가따 포팅메뉴얼

# 1. 개요

---

## 1-1. 프로젝트 개요

- 물류 허브 자동화

## 1-2. 개발 환경

### Backend

- Java : Open JDK 17
- Spring Boot : 3.3.4
- JPA : Jakarta Persistence 3.1.0
- ORM : Hibernate 6.5.3.Final
- DB : MySQL 8.3.0, Redis 3.3.4
- Message Broker : RabbitMQ 3.1.7
- Spring Security: 6.3.3
- JWT : jjwt 0.12.3
- Build Tool : Gradle
- Lombok : 1.18.34

### Frontend

- Vue.js : 3.5.12
- Vite : 5.4.11
- Package Manager : NPM
- Axios : 1.7.7
- Vue Router : 4.4.5
- Pinia : 2.2.6
- TailwindCSS : 3.4.14

## 1-3. 프로젝트 사용 도구

- 이슈 / 형상 관리 : Gitlab
- 코드 리뷰 : Gitlab
- 배포 : Docker, Docker Compose, Nginx
- Design : Figma
- 커뮤니케이션, 일정 관리 : Mattermost, Notion, Jira

## 1-4. 외부 API

- Kakao MAP API

# 2. 설정 파일

---

## 2-1. Backend

- 루트 디렉토리에 .env 설정: 변수 자동 주입 

### 2-1-1. application.yml

```yaml
spring:
  config:
    import: optional:file:.env[.properties]
  application:
    name: wattagatta
  rabbitmq:
    port: 5672
    username: guest
    password: guest
    host: rabbitmq

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql/${MYSQL_DATABASE}?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ${MYSQL_USERNAME}
    password: ${MYSQL_PASSWORD}

  data:
    redis:
      port: 6379

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

  jwt:
    secret: ${JWT_SECRET_KEY}

agent:
  task:
    duration:
      time: 5
  move:
    duration:
      time: 1
  convey:
    duration:
      time: 15

websocket:
  unity:
    path: /ws/rccar
  agent:
    path: /ws/agent
```

## 2-2. Frontend

- 루트 디렉토리내에서 `npm install` 후 `npm run build`를 통해 `/dist` 폴더에 만들어지는 내용물을 통해 확인 및 배포 가능
- .env 설정 후 배포

### 2-2-1. vite.config.js —Vite의 설정 파일

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
})

```

### 2-2-2. package.json

```js
{
  "name": "frontend",
  "private": true,
  "version": "0.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "@heroicons/vue": "^2.1.5",
    "axios": "^1.7.7",
    "jwt-decode": "^4.0.0",
    "pinia": "^2.2.4",
    "vue": "^3.5.12",
    "vue-router": "^4.4.5"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.1.4",
    "autoprefixer": "^10.4.20",
    "postcss": "^8.4.47",
    "tailwindcss": "^3.4.14",
    "vite": "^5.4.9"
  }
}
```
### 2-2-3. tailwind.config.js
```js
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'primary': {
          50: '#f0f9ff',
          100: '#e0f2fe',
          200: '#bae6fd',
          300: '#7dd3fc',
          400: '#38bdf8',
          500: '#0ea5e9',
          600: '#0284c7',
          700: '#0369a1',
          800: '#075985',
          900: '#0c4a6e',
        },
      },
      fontFamily: {
        sans: ['Inter var', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
```

# 3. 배포

---

## 3-1. 준비 사항

- 백엔드 
  - PC에서 최신 버전인지 확인 한 후, clean build하여 새로운 jar 파일 생성 후 `chris429/wattagatta-back:latest` 이미지 빌드 및 푸시.
  - `compose.yml` 파일을 백엔드 경로에 위치시킴.
- 프론트엔드 : ec2의 `/home/ubuntu`에서 레포지토리를 pull 받고 루트 디렉토리에서 `npm install`, `npm run build` 명령어로 빌드하여 `/home/ubuntu/source/build` 경로로 `dist/*`의 내용물을 모두 복사 
- `compose.yml` 파일이 있는 경로에서 `docker compose`로 spring 서버와 db를 실행시킨다. 

### 3-1-1. compose.yml

- `docker compose`를 이용하여 백엔드 서버와 DB를 실행시킨다.

```yaml
services: 
  spring: 
    env_file: .env
    container_name: spring
    image: chris429/wattagatta-back:latest
    pull_policy: always
    ports: 
      - "8081:8080" 
    depends_on: 
      mysql:
        condition: service_healthy
      redis:
        condition: service_healthy
  mysql:
    container_name: mysql
    image: mysql
    ports:
      - "3307:3306"
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
    healthcheck:
      test: [ "CMD", "mysqladmin", "ping" ] 
      interval: 5s 
      retries: 10
    volumes:
      - mysql_data:/var/lib/mysql # 가상 볼륨 사용
  redis:
    container_name: redis
    image: redis
    ports:
      - "6379:6379"
    healthcheck:
      test: [ "CMD", "redis-cli", "ping" ] 
      interval: 5s
      retries: 10
      
volumes: 
  mysql_data:
    name: mysql_data
```

### 3-1-2. Nginx 설정

```shell
server {
        listen 80;
        server_name k11c208.p.ssafy.io;

        # 프론트엔드 서빙
        location / {
                # First attempt to serve request as file, then
                # as directory, then fall back to displaying a 404.
                root /home/ubuntu/source/build; # 복사해둔 빌드 파일
                index index.html;
                try_files $uri $uri/ /index.html;
        }

        # 백엔드 API 요청을 8081 포트로 프록시
        location /api {
                proxy_pass http://localhost:8081;  # 스프링 컨테이너가 8081 포트에서 수신 대기 . 도커 환경에서 localhost는 nginx 컨테이너 내의 localhost이므로, nginx가 spring boot 컨테이너를 프록시하려면 컨테이너 이름 사용해야 함
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }

        # WebSocket 프로토콜 지원 위한 설정
        location /ws {
                proxy_pass http://localhost:8081;
                proxy_http_version 1.1; # WebScoket은 Http/1.1 이상이 필요함
                proxy_set_header Upgrade $http_upgrade; # WS protocol upgrade header
                proxy_set_header Connection "upgrade"; # 연결 업그레이드 설정
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_read_timeout 3600s;
                proxy_send_timeout 3600s;
                # proxy_set_header X-Forwarded-Proto $scheme; # 사용자가 사용한 프로토콜 be에 전달
        }
}

```

- `/etc/nginx/sites-available/` 에 위치한 이 `default` 파일의 내용을 이렇게 설정한 후, nginx 재시작을 한다.