package ch.abbts.smartlodge.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*

data class UserSession(val name: String, val count: Int) : Principal

fun Application.installSessionAndAuthentication() {
  install(Sessions) {
    cookie<UserSession>("user_session") {
      cookie.path = "/"
      cookie.maxAgeInSeconds = 60
    }
  }
  install(Authentication) {
    form("auth-form") {
      userParamName = "username"
      passwordParamName = "password"
      validate { credentials ->
        if (credentials.name == "jetbrains" && credentials.password == "foobar") {
          UserIdPrincipal(credentials.name)
        } else {
          null
        }
      }
    }
    session<UserSession>("auth-session") {
      validate { session ->
        if(session.name.startsWith("jet")) {
          session
        } else {
          null
        }
      }
      challenge {
        call.respondRedirect("/login")
      }
    }
  }

  routing {
    get("/login") {
      call.respondHtml {
        body {
          form(action = "/login", encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
            p {
              +"Username:"
              textInput(name = "username")
            }
            p {
              +"Password:"
              passwordInput(name = "password")
            }
            p {
              submitInput() { value = "Login" }
            }
          }
        }
      }
    }

    authenticate("auth-form") {
      post("/login") {
        val userName = call.principal<UserIdPrincipal>()?.name.toString()
        call.sessions.set(UserSession(name = userName, count = 1))
        call.respondRedirect("/")
      }
    }

    get("/logout") {
      call.sessions.clear<UserSession>()
      call.respondRedirect("/login")
    }
  }
}
