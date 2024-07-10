package ch.abbts.smartlodge.routes

import ch.abbts.smartlodge.plugins.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(val username: String)

fun Application.configureSinglePageApplication() {
  routing {
    authenticate("auth-session") {
      singlePageApplication { vue("src/main/vue-project/dist") }

      get("/user-info") {
        val userSession = call.principal<UserSession>()
        call.sessions.set(userSession?.copy(count = userSession.count + 1))
        call.respond(UserInfo(userSession?.name ?: "???"))
      }
    }
  }
}
