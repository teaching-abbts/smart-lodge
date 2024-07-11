package ch.abbts.smartlodge.routes

import ch.abbts.smartlodge.plugins.UserSession
import ch.abbts.smartlodge.services.SmartHomeDataService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(val username: String)

fun Application.configureSinglePageApplication(smartHomeDataService: SmartHomeDataService) {
  routing {
    authenticate("auth-session") {
      singlePageApplication { vue("src/main/vue-project/dist") }

      get("/user-info") {
        val principal = call.principal<UserSession>();
        val userName = principal?.name.toString()
        call.respond(HttpStatusCode.OK, UserInfo(userName))
      }

      get("/get-smart-homes") {
        val smartHomes = smartHomeDataService.getSmartHomes()
        call.respond(HttpStatusCode.OK, smartHomes)
      }

      get("/get-smart-home-measurements") {
        val measurements = smartHomeDataService.getMeasurements()
        call.respond(HttpStatusCode.OK, measurements)
      }

      get("/get-smart-home-measurment/{buildingID}") {
        val buildingId = call.parameters["buildingID"] ?: ""
        val measurements = smartHomeDataService.getMeasurementsForBuildingId(buildingId)
        call.respond(HttpStatusCode.OK, measurements)
      }
    }
  }
}
