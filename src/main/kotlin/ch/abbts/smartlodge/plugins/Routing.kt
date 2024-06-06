package ch.abbts.smartlodge.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
  routing {
    singlePageApplication { vue("src/main/vue-project/dist") }

    // Static plugin. Try to access `/static/index.html`
    staticFiles("/static", File("src/main/resources/static"))
  }
}
