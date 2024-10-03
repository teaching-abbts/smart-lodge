package ch.abbts.smartlodge.plugins

import ch.abbts.smartlodge.services.AuthenticationService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
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
      challenge {
        call.respondRedirect(LOGIN_URL)
      }
    }
    session<UserSession>("auth-session") {
      validate { session ->
        session
      }
      challenge {
        call.respondRedirect(LOGIN_URL)
      }
    }
  }

  data class LoginModel(val postUrl: String, val userParamName: String, val passwordParamName: String)

  routing {
    get(LOGIN_URL) {
      val loginModel = LoginModel(LOGIN_URL, USER_PARAM_NAME, PASSWORD_PARAM_NAME)
      call.respond(FreeMarkerContent("login.ftl", mapOf("login" to loginModel)))
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
