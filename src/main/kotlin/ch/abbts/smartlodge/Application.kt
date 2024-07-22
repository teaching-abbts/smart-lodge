package ch.abbts.smartlodge

import ch.abbts.smartlodge.plugins.*
import ch.abbts.smartlodge.services.SmartHomeDataService
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main() {
  val enableHttps = System.getenv("DISABLE_HTTPS") != "true"

  coroutineScope {
    val httpPort = System.getenv("HTTP_PORT").toIntOrNull() ?: 8080
    val httpsPort = System.getenv("HTTPS_PORT").toIntOrNull() ?: 8443
    val hostBinding = System.getenv("HOST_BINDING") ?: "0.0.0.0"
    val dataServiceHostname = System.getenv("DATA_SERVICE_HOSTNAME") ?: "localhost"
    val dataServicePort = System.getenv("DATA_SERVICE_PORT").toIntOrNull() ?: 11001
    val dataServiceUseHttps = System.getenv("DATA_SERVICE_USE_HTTPS") == "true"

    val smartHomeDataService = SmartHomeDataService(dataServiceHostname, dataServicePort, dataServiceUseHttps)

    launch {
      smartHomeDataService.start()
    }

    val appEnginEnv = applicationEngineEnvironment {
      module {
        installKoinDependencyInjection()

        if (enableHttps) {
          installHttpsRedirect(httpsPort)
        }

        installForwardedHeaders()
        installSessionAndAuthentication()
        installFreeMarkerTemplating()
        installSerialization()

        configureRouting(smartHomeDataService)
      }

      configureHttp(hostBinding, httpPort)

      if (enableHttps) {
        configureHttps(hostBinding, httpsPort)
      }

      watchPaths = listOf("classes", "resources")
    }

    embeddedServer(Netty, appEnginEnv)
      .start(wait = true)
  }
}
