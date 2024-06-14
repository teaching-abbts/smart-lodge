package ch.abbts.smartlodge.plugins

import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.*
import io.ktor.server.freemarker.*

fun Application.configureTemplating() {
  install(FreeMarker) {
    templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
  }
}
