package ch.abbts.smartlodge.plugins

import ch.abbts.smartlodge.routes.configureFotoalbum
import ch.abbts.smartlodge.routes.configureSinglePageApplication
import io.ktor.server.application.*

fun Application.configureRouting() {
  configureSinglePageApplication()
  configureFotoalbum()
}
