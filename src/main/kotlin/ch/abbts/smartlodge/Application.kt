package ch.abbts.smartlodge

import ch.abbts.smartlodge.plugins.configureRouting
import ch.abbts.smartlodge.plugins.configureSerialization
import ch.abbts.smartlodge.plugins.configureTemplating
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

const val HTTP_PORT = 8080
const val HOST_BINDING = "0.0.0.0"

fun Application.hostModule() {
  configureTemplating()
  configureSerialization()
  configureRouting()
}

fun ApplicationEngineEnvironmentBuilder.envConfig() {
  module { hostModule() }

  connector {
    host = HOST_BINDING
    port = HTTP_PORT
  }

  watchPaths = listOf("classes", "resources")
}

fun main() {
  val appEnginEnv = applicationEngineEnvironment { envConfig() }

  embeddedServer(Netty, appEnginEnv)
    .start(wait = true)
}
