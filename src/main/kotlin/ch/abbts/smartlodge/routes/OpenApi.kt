package ch.abbts.smartlodge.routes

import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.generators.html.*

fun Application.configureOpenApi() {
  val swaggerFilePath = "src/main/resources/openapi/documentation.yaml"

  routing {
    openAPI(path = "openapi", swaggerFile = swaggerFilePath) {
      codegen = StaticHtml2Codegen()
    }
    swaggerUI(path = "swagger", swaggerFile = swaggerFilePath) {
      version = "5.17.14"
    }
  }
}
