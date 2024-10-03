package ch.abbts.smartlodge.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.Closeable

@Serializable
data class Event(
  val timeStamp: String,  // Format: YYYY-MM-DD hh:mm:ss
  val buildingID: String,
  val type: String,
  val data: String
) {
}

@Serializable
data class Measurement(
  val timeStamp: String,  // Format: YYYY-MM-DD hh:mm:ss
  val buildingID: String,
  val brightness: Int,
  val temperature: Int,
  val humidity: Int,
  val gas: Int
)

@Serializable
data class SmartHomeData(
  val measurements: List<Measurement>,
  val events: List<Event>
)

@Serializable
data class SmartHome(val buildingID: String)

class SmartHomeDataService(
  private val hostname: String = "127.0.0.1",
  private val port: Int = 11001,
  private val useHttps: Boolean = false,
  private val jobDelay: Long = 10_000L
) : Closeable {
  private val httpClient: HttpClient = HttpClient(CIO) {
    install(Resources)
    install(ContentNegotiation) {
      json()
    }
  }
  private var data: SmartHomeData? = null
  private var job: Job? = null

  private suspend fun fetchData() {
    try {
      val protocol = if (useHttps) "https" else "http"
      val response = httpClient.get("$protocol://$hostname:$port/smart-quartier/data-service/history")

      if (response.status == HttpStatusCode.OK) {
        data = response.body<SmartHomeData>()
      }
    } catch (e: Exception) {
      println(e.message)
    }
  }

  suspend fun start() = coroutineScope {
    if (job == null) {
      job = launch {
        while (true) {
          fetchData()
          delay(jobDelay)
        }
      }
    }
  }

  fun stop() {
    if (job?.isActive == true) {
      job?.cancel()
      job = null
    }
  }

  private fun getGroupedMeasurements(): Map<String, List<Measurement>>? {
    val measurementGroups = data?.measurements?.groupBy(keySelector = { it.buildingID })

    return measurementGroups
  }

  fun getSmartHomes(): List<SmartHome> {
    val measurementGroups = getGroupedMeasurements()
    val smartHomes = measurementGroups?.keys?.map { buildingId -> SmartHome(buildingId) }

    return smartHomes ?: emptyList()
  }

  fun getMeasurements(): List<Measurement> {
    return data?.measurements ?: emptyList()
  }

  fun getMeasurementsForBuildingId(buildingId: String): List<Measurement> {
    val measurementGroups = getGroupedMeasurements()
    return measurementGroups?.get(buildingId) ?: return emptyList()
  }

  override fun close() {
    stop()
  }
}
