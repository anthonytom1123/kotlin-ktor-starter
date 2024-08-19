package io.initialcapacity.collector

import io.initialcapacity.workflow.Worker
import io.initialcapacity.analyzer.Task
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.*
import org.slf4j.LoggerFactory
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.protobuf.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import javax.print.attribute.standard.Compression


class ExampleWorker(override val name: String = "data-collector") : Worker<ExampleTask> {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    @Volatile
    private var busy: Boolean = false;

    override fun isBusy(): Boolean {
        return busy
    }

    override fun setBusy(v: Boolean) {
        busy = v
    }

    override fun execute(task: ExampleTask) {
        logger.info("Executing task")
        val body: ByteArray = collectData(getUrl())
        val taskList = processBody(body)
        sendToAnalyser(taskList)
    }

    private fun getUrl(): String {
        var url = "https://api.511.org/transit/StopMonitoring?api_key="
        url += System.getenv("TransitToken")
        url += "&agency=SF"
        return url
    }

    fun collectData(url: String): ByteArray =
        runBlocking {
            logger.info("starting data collection.")
            val response: HttpResponse = HttpClient(CIO) {
                install(ContentEncoding) {
                    gzip(0.9F)
                }
                install(ContentNegotiation) {
                    json()
                }
            }.use { client ->
                client.get(url) {}
            }
            if (response.status.value in 200..299) {
                logger.info("Successful response at ${response.request.url}")
            }
            return@runBlocking response.body()
        }

    private fun processBody(body: ByteArray): MutableList<Task> {
        //substring to remove BOM
        logger.info("processing body")
        val obj = Json.parseToJsonElement(body.toString(Charsets.UTF_8).substring(1)).jsonObject
        val stopData = obj["ServiceDelivery"]?.jsonObject?.get("StopMonitoringDelivery")?.jsonObject?.get("MonitoredStopVisit")?.jsonArray
        logger.info("Collector fetched ${stopData?.size} data points")
        val taskList: MutableList<Task> = mutableListOf()
        stopData?.forEach { stopVisit ->
            val stopJsonObj = stopVisit.jsonObject
            val monitoredVehicleJourney = stopJsonObj["MonitoredVehicleJourney"]?.jsonObject
            val monitoredCall = monitoredVehicleJourney?.get("MonitoredCall")?.jsonObject
            val data = Task(
                monitoredVehicleJourney?.get("LineRef").toString(),
                monitoredVehicleJourney?.get("PublishedLineName").toString(),
                monitoredCall?.get("StopPointRef").toString().replace("\"","").toInt(),
                monitoredCall?.get("StopPointName").toString(),
                monitoredVehicleJourney?.get("DirectionRef").toString(),
                monitoredVehicleJourney?.get("Occupancy").toString(),
                monitoredCall?.get("ExpectedArrivalTime").toString()
            )
            taskList.addLast(data)
        }
        return taskList
    }

    private fun sendToAnalyser(taskList: MutableList<Task>) =
        runBlocking {
            val url: String = System.getenv("ANALYZER_URL")
            val response: HttpResponse = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json()
                }
            }.use { client ->
                logger.info("Attempting to connect to $url")
                client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(taskList))
                }
            }
            return@runBlocking response
        }
}