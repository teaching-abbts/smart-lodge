import { fileURLToPath, URL } from 'node:url'
import process from 'process'
import { defineConfig, type Plugin } from 'vite'
import vue from '@vitejs/plugin-vue'
import Inspect from 'vite-plugin-inspect'
import psList from 'ps-list'

function monitorParentProcessPlugin(): Plugin {
  return {
    name: 'vite-plugin-monitor-parent-process',
    options: {
      order: 'pre',
      handler(options) {
        const parentProcessId = process.ppid
        const processId = process.pid
        const msg = `*** parentProcessId: ${parentProcessId}, processId: ${processId}`
        console.log(msg)
        console.log(`*** PID: ${process.env.PID}`)

        if (process.env.PID) {
          psList().then((response) => {
            console.log(
              `*** ${JSON.stringify(
                response.filter((p) => p.pid.toString() == process.env.PID),
                null,
                2
              )}`
            )
          })
        }
      }
    }
    // configureServer(server) {
    //   const parentProcessId = process.ppid
    //   fs.writeFile('output.txt', `*** parentProcessId: ${parentProcessId}`, (error) => {})
    //   // const terminateViteServer = () => {
    //   //   if (server.httpServer) {
    //   //     server.httpServer.close(() => {
    //   //       console.log('*** Vite server closed due to parent process termination');
    //   //       process.exit(1);
    //   //     });
    //   //   }
    //   // };
    //   //
    //   // process.on("disconnect", () => {
    //   //   console.log('*** Parent process disconnected');
    //   //   terminateViteServer();
    //   // })
    //
    //   // const handleSignal = () => {
    //   //   process.exit();
    //   // };
    //   //
    //   // process.on("disconnect", handleSignal);
    //   // process.on("exit", handleSignal)
    //   // process.on('SIGINT', handleSignal);
    //   // process.on('SIGTERM', handleSignal);
    // }
  }
}

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    Inspect({
      build: true,
      outputDir: '.vite-inspect'
    }),
    monitorParentProcessPlugin()
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
