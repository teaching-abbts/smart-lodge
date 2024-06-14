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

tasks.register<Exec>("build-vue") {
  group = "build"
  description = "builds the vue-project."
  workingDir = File("src/main/vue-project")

  val npmScript = "build"

  if (System.getProperty("os.name").lowercase().contains("windows")) {
    commandLine("cmd.exe", "/C", "npm run $npmScript")
  }
  else {
    commandLine("npm", "run", npmScript)
  }
}

tasks.register<Exec>("run-vue-watch") {
  group = "application"
  description = "builds the vue-project continuously."
  workingDir = File("src/main/vue-project")

  val npmScript = "build-watch"

  if (System.getProperty("os.name").lowercase().contains("windows")) {
    commandLine("cmd.exe", "/C", "npm run $npmScript")
  }
  else {
    commandLine("npm", "run", npmScript)
  }
}

tasks.register<Exec>("build-continuously") {
  group = "application"
  description = "builds the Ktor project continuously."
  workingDir = File(".")

  if (System.getProperty("os.name").lowercase().contains("windows")) {
    commandLine("cmd.exe", "/C", "gradlew.bat", "build", "-t")
  }
  else {
    commandLine("gradlew", "build", "-t", "--parallel")
  }
}

// tasks.register<GradleBuild>("DEVELOP") {
//   group = "application"
//   description = "runs all necessary tasks at once."
//   tasks = listOf("build-continuously", "run-vue-watch", "run")
// }
