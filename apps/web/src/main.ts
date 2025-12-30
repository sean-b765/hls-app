import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './index.css'

import router from './router'
import App from './App.vue'
import { useBootstrapStore } from './stores/bootstrap'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')

useBootstrapStore()
