package ch.abbts.smartlodge

import ch.abbts.smartlodge.plugins.*
import ch.abbts.smartlodge.services.SmartHomeDataService
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

const val HTTP_PORT = 8080
const val HTTPS_PORT = 8443
const val HOST_BINDING = "0.0.0.0"

suspend fun main() {
  coroutineScope {
    val smartHomeDataService = SmartHomeDataService()

    launch {
      smartHomeDataService.start()
    }

    val appEnginEnv = applicationEngineEnvironment {
      module {
        installKoinDependencyInjection()
        installHttps(HTTPS_PORT)
        installSessionAndAuthentication()
        installFreeMarkerTemplating()
        installSerialization()

        configureRouting(smartHomeDataService)
      }

      configureHttp(HOST_BINDING, HTTP_PORT)
      configureHttps(HOST_BINDING, HTTPS_PORT)

      watchPaths = listOf("classes", "resources")
    }

    embeddedServer(Netty, appEnginEnv)
      .start(wait = true)
  }
}
