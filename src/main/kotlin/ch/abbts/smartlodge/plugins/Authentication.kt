package ch.abbts.smartlodge.plugins

import ch.abbts.smartlodge.security.AuthenticationService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import org.koin.ktor.ext.inject

data class UserSession(val name: String) : Principal

const val LOGIN_URL = "/login"

fun Application.installSessionAndAuthentication() {
  val authenticationService by inject<AuthenticationService>()


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
      validate { credential ->
        authenticationService.authenticate(credential)
      }
    }
    session<UserSession>("auth-session") {
      validate { session ->
//        if (session.name.startsWith("jet")) {
//          session
//        } else {
//          null
//        }
        session
      }
      challenge {
        call.respondRedirect(LOGIN_URL)
      }
    }
  }

  routing {
    get(LOGIN_URL) {
      call.respondHtml {
        body {
          form(action = LOGIN_URL, encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
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
      post(LOGIN_URL) {
        val userName = call.principal<UserIdPrincipal>()?.name.toString()
        val userSession = UserSession(name = userName)
        call.sessions.set(userSession)
        call.respondRedirect("/")
      }
    }

    get("/logout") {
      call.sessions.clear<UserSession>()
      call.respondRedirect(LOGIN_URL)
    }
  }
}
