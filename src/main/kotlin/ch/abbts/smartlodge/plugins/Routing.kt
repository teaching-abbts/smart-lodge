package ch.abbts.smartlodge.plugins

import ch.abbts.smartlodge.routes.configureFotoalbum
import ch.abbts.smartlodge.routes.configureOpenApi
import ch.abbts.smartlodge.routes.configureSinglePageApplication
import ch.abbts.smartlodge.services.SmartHomeDataService
import io.ktor.server.application.*

fun Application.configureRouting(smartHomeDataService: SmartHomeDataService) {
  configureSinglePageApplication(smartHomeDataService)
  configureOpenApi()
  configureFotoalbum()
}
