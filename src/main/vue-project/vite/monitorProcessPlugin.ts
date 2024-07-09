import type { Plugin } from 'vite'
import pidStatus, { Status } from 'pidusage'
import { EventEmitter } from 'node:events'

export class ProcessObserver extends EventEmitter {
  private intervalId: NodeJS.Timeout | null = null

  constructor(
    private pid: number,
    private interval = 1000,
    private observing = false
  ) {
    super()
  }

  start() {
    if (this.observing) {
      return
    }

    this.observing = true
    this.intervalId = setInterval(() => {
      pidStatus(this.pid, (err, stats) => {
        if (err) {
          this.emit('error', err)
        } else {
          this.emit('data', stats)
        }
      })
    }, this.interval)
  }

  stop() {
    if (!this.observing) {
      return
    }

    if (this.intervalId) {
      clearInterval(this.intervalId)
    }

    this.observing = false
  }

  onData(callback: (data: Status) => void) {
    this.on('data', callback)
  }

  onError(callback: (error: Error) => void) {
    this.on('error', callback)
  }
}

type MonitorParentProcessPluginConfig = {
  pid: number
  onData?: (data: Status) => void
  onError?: (error: Error) => void
}

export function monitorProcessPlugin(
  config: MonitorParentProcessPluginConfig
): Plugin<MonitorParentProcessPluginConfig> {
  return {
    name: 'vite-plugin-monitor-parent-process',
    options() {
      const processObserver = new ProcessObserver(config.pid)

      if (config.onData) {
        processObserver.onData(config.onData)
      }

      if (config.onError) {
        processObserver.onError(config.onError)
      }

      console.log(`*** Start observing PID '${config.pid}'...`)
      processObserver.start()
    }
  }
}
