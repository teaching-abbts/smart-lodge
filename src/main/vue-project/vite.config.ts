import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Inspect from 'vite-plugin-inspect'
import { syncLifetimeToPidPlugin } from './vite/syncLifetimeToPidPlugin'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    Inspect({
      build: true,
      outputDir: '.vite-inspect'
    }),
    syncLifetimeToPidPlugin()
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {
    sourcemap: true
  }
})
