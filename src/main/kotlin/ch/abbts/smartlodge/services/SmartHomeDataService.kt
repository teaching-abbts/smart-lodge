package ch.abbts.smartlodge.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
data class Event(
  val timeStamp: String,  // Format: YYYY-MM-DD hh:mm:ss
  val buildingID: String,
  val type: String,
  val data: String) {
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

class SmartHomeDataService {
  private val httpClient: HttpClient = HttpClient(CIO) {
    install(Resources)
    install(ContentNegotiation) {
      json()
    }
  }
  private var data: SmartHomeData? = null

  private suspend fun fetchData() {
    val response = httpClient.get("http://127.0.0.1:11001/smart-quartier/data-service/history")

    if (response.status == HttpStatusCode.OK) {
      data = response.body<SmartHomeData>()
    }
  }

  suspend fun start() {
    fetchData()
    delay(10_000L)
    start()
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
}
