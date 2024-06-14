@file:Suppress("PropertyName")

val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project

plugins {
  kotlin("jvm") version "2.0.0"
  kotlin("plugin.serialization") version "2.0.0"
  id("io.ktor.plugin") version "2.3.11"
}

group = "ch.abbts.smartlodge"
version = "0.0.1"

application {
  mainClass.set("ch.abbts.smartlodge.ApplicationKt")

  val isDevelopment: Boolean = project.ext.has("development")
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
  implementation("io.ktor:ktor-server-core-jvm")
  implementation("io.ktor:ktor-server-freemarker-jvm")
  implementation("io.ktor:ktor-server-content-negotiation-jvm")
  implementation("io.ktor:ktor-server-host-common-jvm")
  implementation("io.ktor:ktor-server-http-redirect-jvm")
  implementation("io.ktor:ktor-server-swagger-jvm")
  implementation("io.ktor:ktor-server-netty-jvm")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
  implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktor_version")
  implementation("ch.qos.logback:logback-classic:$logback_version")
  testImplementation("io.ktor:ktor-server-tests-jvm")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

data class CommandLineConfig(val cmd: String, val windowsCmd: String? = null)

fun Exec.runCommandLine(commandLineConfig: CommandLineConfig) {
  val isWindows = System.getProperty("os.name").lowercase().contains("windows")

  if (isWindows) {
    commandLine("cmd.exe", "/C", commandLineConfig.windowsCmd ?: commandLineConfig.cmd)
  } else {
    commandLine(commandLineConfig.cmd.splitToSequence(' ').toList())
  }
}

fun Exec.runCommandLine(vararg arguments: String) {
  runCommandLine(CommandLineConfig(arguments.joinToString(" ")))
}

tasks.register<Exec>("build-vue") {
  group = "build"
  description = "builds the vue-project."
  workingDir = File("src/main/vue-project")

  runCommandLine("npm", "run", "build")
}

tasks.register<Exec>("run-vue-watch") {
  group = "application"
  description = "builds the vue-project continuously."
  workingDir = File("src/main/vue-project")

  runCommandLine("npm", "run", "build-watch")
}

tasks.register<Exec>("build-continuously") {
  group = "application"
  description = "builds the Ktor project continuously."
  workingDir = File(".")

  val config = CommandLineConfig("gradlew build -t", "gradlew.bat build -t")
  runCommandLine(config)
}
