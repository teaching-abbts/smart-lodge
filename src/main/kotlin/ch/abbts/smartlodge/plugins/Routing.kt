package ch.abbts.smartlodge.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlinx.serialization.*
import kotlinx.serialization.json.*

const val IMAGE_UPLOAD_DIRECTORY = "src/main/resources/images"

@Serializable data class Foto(val url: String, val name: String)

@Serializable data class Fotoalbum(val fotos: List<Foto>)

fun Application.configureRouting() {
  routing {
    singlePageApplication { vue("src/main/vue-project/dist") }

    get("/image/{imageName}") {
      val imageName = call.parameters["imageName"]
      call.respondFile(File("$IMAGE_UPLOAD_DIRECTORY/$imageName"))
    }

    get("/fotoalbum") {
      val directory = File(IMAGE_UPLOAD_DIRECTORY)
      val images =
          directory.listFiles()?.filter { it.isFile and it.name.endsWith(".jpg") } ?: emptyList()
      val fotos = mutableListOf<Foto>()

      for (image in images) {
        val url = "/image/" + image.name
        val name = image.nameWithoutExtension
        val newFoto = Foto(url, name)

        fotos.add(newFoto)
      }

      val response = Fotoalbum(fotos)
      call.respond(response)
    }

    post("/upload-images") {
      val multipartData = call.receiveMultipart()

      multipartData.forEachPart { part ->
        when (part) {
          is PartData.FileItem -> {
            val fileName = part.originalFileName as String
            val fileBytes = part.streamProvider().readBytes()
            val file = File("$IMAGE_UPLOAD_DIRECTORY/$fileName")
            // Ensure the parent directory exists
            Files.createDirectories(file.toPath().parent)
            Files.write(file.toPath(), fileBytes, StandardOpenOption.CREATE)
          }
          else -> {}
        }
        part.dispose()
      }

      call.response.status(HttpStatusCode.OK)
    }

    // Static plugin. Try to access `/static/index.html`
    staticFiles("/static", File("src/main/resources/static"))
  }
}
