package ch.abbts.smartlodge.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.util.*

fun Application.installBasicAuthentication() {
  install(Authentication) {
    basic("auth-basic") {
      val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }
      val hashedUserTable = UserHashedTableAuth(
        table = mapOf(
          "foo" to digestFunction("bar"), "admin" to digestFunction("password")
        ), digester = digestFunction
      )

      realm = "Access to the '/' path"
      validate { credentials ->
        hashedUserTable.authenticate(credentials)
      }
    }
  }
}
