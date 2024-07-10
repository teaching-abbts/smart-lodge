package ch.abbts.smartlodge

import ch.abbts.smartlodge.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

const val HTTP_PORT = 8080
const val HTTPS_PORT = 8443
const val HOST_BINDING = "0.0.0.0"

fun main() {
  val appEnginEnv = applicationEngineEnvironment {
    module {
      installHttps(HTTPS_PORT)
      installSessionAndAuthentication()
      installFreeMarkerTemplating()
      installSerialization()
      configureRouting()
    }

    configureHttp(HOST_BINDING, HTTP_PORT)
    configureHttps(HOST_BINDING, HTTPS_PORT)

    watchPaths = listOf("classes", "resources")
  }

  embeddedServer(Netty, appEnginEnv)
    .start(wait = true)
}
