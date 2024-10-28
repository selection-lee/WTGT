import { createApp } from 'vue'
import {createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
// import './style.css'
import './assets/index.css' // Tailwind CSS 스타일

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.mount('#app')
