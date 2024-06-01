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

fun main() {
    val env = applicationEngineEnvironment {
        envConfig()
    }
    embeddedServer(Netty, env)
        .start(wait = true)
}

fun Application.hostModule() {
    configureTemplating()
    configureSerialization()
    configureHTTP()
    configureRouting()
}

fun ApplicationEngineEnvironmentBuilder.envConfig() {
    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }

    keyStore.saveToFile(keyStoreFile, "123456")

    module {
        hostModule()
    }

    connector {
        host = "0.0.0.0"
        port = 8080
    }

    sslConnector(
        keyStore = keyStore,
        keyAlias = "sampleAlias",
        keyStorePassword = { "123456".toCharArray() },
        privateKeyPassword = { "foobar".toCharArray() }) {
        port = 8443
        keyStorePath = keyStoreFile
    }
}
