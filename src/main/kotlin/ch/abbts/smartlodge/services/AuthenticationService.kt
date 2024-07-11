package ch.abbts.smartlodge.services

import io.ktor.server.auth.*
import io.ktor.util.*

class AuthenticationService {
  private val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }
  private val usernameToPasswordMap = mutableMapOf<String, ByteArray>()

  init {
    usernameToPasswordMap["jetbrains"] = digestFunction("foobar")
    usernameToPasswordMap["admin"] = digestFunction("password")
  }

  fun authenticate(credential: UserPasswordCredential): UserIdPrincipal? {
    return if (usernameToPasswordMap.containsKey(credential.name) && usernameToPasswordMap[credential.name].contentEquals(
        digestFunction(credential.password)
      )
    ) {
      UserIdPrincipal(credential.name)
    } else {
      null
    }
  }
}
