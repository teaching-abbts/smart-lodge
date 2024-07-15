import { type Plugin } from 'vite'
import { monitorProcessPlugin, ProcessObserver } from './monitorProcessPlugin'
import psList from 'ps-list'

type SyncLifetimeToPidPluginConfig = { envVarName: string; observeChildren: boolean }

/**
 * Observes a given process for termination - if said process terminates, this plugin ensures that this node.js process is properly terminated as well.
 *
 * Reasoning:
 * Due to the way gradle runs shell commands on windows, node.js child-processes are not properly terminated when debugging is stopped.
 * @param config
 */
export function syncLifetimeToPidPlugin(config?: SyncLifetimeToPidPluginConfig): Plugin {
  const envVarName = config?.envVarName ?? 'PID'
  const rawPid = process.env[envVarName]
  const pid = Number(rawPid)
  const isPidInvalid = isNaN(pid)
  const observeChildren = config?.observeChildren ?? true

  if (isPidInvalid) {
    return {
      name: 'dummy-plugin',
      options() {
        console.log(`*** '${envVarName}' is not a number: '${rawPid}' - skipping PID observation!`)
      }
    }
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
