import { createApp } from 'vue'
import App from './App.vue'
import { router } from '@/routes/router'
import { vuetify } from './vuetify'

createApp(App).use(router).use(vuetify).mount('#app')
