import { createRouter, createWebHashHistory } from 'vue-router'
import HomeVue from '@/routes/HomeRoute.vue'

const routes = [
  { path: '/', component: HomeVue },
  { path: '/fotoalbum', component: () => import('./FotoalbumRoute.vue') }
]

export const router = createRouter({
  routes,
  history: createWebHashHistory()
})
