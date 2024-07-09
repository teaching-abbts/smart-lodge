import { type Plugin } from 'vite'
import { monitorProcessPlugin, ProcessObserver } from './monitorProcessPlugin'
import psList from 'ps-list'

type SyncLifetimeToPidPluginConfig = { envVarName: string; observeChildren: boolean }

export function syncLifetimeToPidPlugin(config?: SyncLifetimeToPidPluginConfig): Plugin {
  const envVarName = config?.envVarName ?? 'PID'
  const rawPid = process.env[envVarName]
  const pid = Number(rawPid)
  const isPidInvalid = isNaN(pid)
  const observeChildren = config?.observeChildren ?? true

  if (isPidInvalid) {
    throw new Error(`*** '${envVarName}' is not a number: '${rawPid}'`)
  } else {
    const childProcessObservers: ProcessObserver[] = []

    const createErrorHandle = (pid: number) => {
      return () => {
        childProcessObservers.forEach((cpo) => cpo.stop())
        console.error(
          `*** PID ${pid} has left us behind without saying goodbye... 😢 - self exiting!`
        )
        process.kill(process.pid)
      }
    }

    if (observeChildren) {
      psList().then((allRunningProcesses) => {
        allRunningProcesses
          .filter((p) => p.ppid === pid)
          .forEach((p) => {
            const childProcessObserver = new ProcessObserver(p.pid)

            childProcessObserver.onError(createErrorHandle(p.pid))

            console.log(`*** Start observing Child PID '${p.pid}'...`)
            childProcessObserver.start()

            childProcessObservers.push(childProcessObserver)
          })
      })
    }

    return monitorProcessPlugin({
      pid,
      onError: createErrorHandle(pid)
    })
  }
}
