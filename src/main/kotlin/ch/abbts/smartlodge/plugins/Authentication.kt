package ch.abbts.smartlodge.plugins

import ch.abbts.smartlodge.services.AuthenticationService
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
const val USER_PARAM_NAME = "username"
const val PASSWORD_PARAM_NAME = "password"

fun Application.installSessionAndAuthentication() {
  val authenticationService by inject<AuthenticationService>()


  install(Sessions) {
    cookie<UserSession>("user_session") {
      cookie.path = "/"
      cookie.maxAgeInSeconds = 300
    }
  }

  install(Authentication) {
    form("auth-form") {
      userParamName = USER_PARAM_NAME
      passwordParamName = PASSWORD_PARAM_NAME
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
              textInput(name = USER_PARAM_NAME)
            }
            p {
              +"Password:"
              passwordInput(name = PASSWORD_PARAM_NAME)
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
        val principal = call.principal<UserIdPrincipal>();
        val userName = principal?.name.toString()
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
