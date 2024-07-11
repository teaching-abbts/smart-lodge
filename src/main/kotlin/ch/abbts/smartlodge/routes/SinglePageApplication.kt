package ch.abbts.smartlodge.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(val username: String)

fun Application.configureSinglePageApplication() {
  routing {
    authenticate("auth-session") {
      singlePageApplication { vue("src/main/vue-project/dist") }

      get("/user-info") {
        val userName = call.principal<UserIdPrincipal>()?.name.toString()
        call.respond(UserInfo(userName))
      }
    }
  }
}
