package ch.abbts.smartlodge.plugins

import ch.abbts.smartlodge.security.AuthenticationService
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

var defaultKoinModule = module {
  singleOf(::AuthenticationService)
}

fun Application.installKoinDependencyInjection() {
  install(Koin) {
    modules(defaultKoinModule)
  }
}
