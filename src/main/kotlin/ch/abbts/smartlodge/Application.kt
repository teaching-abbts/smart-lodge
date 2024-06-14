package ch.abbts.smartlodge

import ch.abbts.smartlodge.plugins.configureHTTP
import ch.abbts.smartlodge.plugins.configureRouting
import ch.abbts.smartlodge.plugins.configureSerialization
import ch.abbts.smartlodge.plugins.configureTemplating
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

const val KEY_STORE_PASSWORD = "123456"
const val KEY_ALIAS = "sampleAlias"
const val PRIVATE_KEY_PASSWORD = "foobar"
const val HTTP_PORT = 8080
const val HTTPS_PORT = 8443
const val HOST_BINDING = "0.0.0.0"

fun Application.hostModule() {
  configureTemplating()
  configureSerialization()
  configureHTTP()
  configureRouting()
}

fun ApplicationEngineEnvironmentBuilder.envConfig() {

  val keyStoreFile = File("build/keystore.jks")
  val keyStore = buildKeyStore {
    certificate(KEY_ALIAS) {
      password = PRIVATE_KEY_PASSWORD
      domains = listOf("127.0.0.1", "localhost", HOST_BINDING)
    }
  }

  keyStore.saveToFile(keyStoreFile, KEY_STORE_PASSWORD)

  module { hostModule() }

  connector {
    host = HOST_BINDING
    port = HTTP_PORT
  }

  sslConnector(
      keyStore = keyStore,
      keyAlias = KEY_ALIAS,
      keyStorePassword = { KEY_STORE_PASSWORD.toCharArray() },
      privateKeyPassword = { PRIVATE_KEY_PASSWORD.toCharArray() }
  ) {
    port = HTTPS_PORT
    keyStorePath = keyStoreFile
  }

  watchPaths = listOf("classes", "resources")
}

fun main() {
  val envConfig = applicationEngineEnvironment { envConfig() }

  embeddedServer(Netty, envConfig)
    .start(wait = true)
}
