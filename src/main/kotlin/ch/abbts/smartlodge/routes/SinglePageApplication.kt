package ch.abbts.smartlodge.routes

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureSinglePageApplication() {
  routing {
    singlePageApplication { vue("src/main/vue-project/dist") }
  }
}
