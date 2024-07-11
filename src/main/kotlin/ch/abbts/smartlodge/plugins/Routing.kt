package ch.abbts.smartlodge.plugins

import ch.abbts.smartlodge.routes.configureFotoalbum
import ch.abbts.smartlodge.routes.configureOpenApi
import ch.abbts.smartlodge.routes.configureSinglePageApplication
import io.ktor.server.application.*

fun Application.configureRouting() {
  configureSinglePageApplication()
  configureOpenApi()
  configureFotoalbum()
}
