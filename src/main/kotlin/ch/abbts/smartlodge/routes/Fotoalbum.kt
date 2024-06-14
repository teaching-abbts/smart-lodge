package ch.abbts.smartlodge.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption

const val IMAGE_UPLOAD_DIRECTORY = "src/main/resources/images"

@Serializable
data class Foto(val url: String, val name: String)

@Serializable
data class Fotoalbum(val fotos: List<Foto>)

fun Application.configureFotoalbum() {
  routing {
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
        val url = "/image/${image.name}"
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
        println("*** Uploading ${part.contentType}, ${part.name}")

        when (part) {
          is PartData.FileItem -> {
            val fileName = part.originalFileName as String
            println("*** originalFileName: $fileName")

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
  }
}
