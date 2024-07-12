package ch.abbts.smartlodge.plugins

import ch.abbts.smartlodge.services.*
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

// https://insert-koin.io/
var defaultKoinModule = module {
  singleOf(::AuthenticationService)
}

fun Application.installKoinDependencyInjection() {
  install(Koin) {
    modules(defaultKoinModule)
  }
}
