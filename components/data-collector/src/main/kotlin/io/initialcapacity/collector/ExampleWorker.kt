package io.initialcapacity.collector

import io.initialcapacity.workflow.Worker
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.compression.*
import io.ktor.http.*


class ExampleWorker(override val name: String = "data-collector") : Worker<ExampleTask> {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun execute(task: ExampleTask) {
        collectData()
    }

    private fun collectData() {
        runBlocking {
            logger.info("starting data collection.")
            // operator_id = "sf"
            //Get all the busses in muni
            //https://api.511.org/transit/lines?api_key=ea43df0e-f500-4422-83a5-f00910b6b2d5&operator_id=SF
            // line_id = 29
            // Get all the stops in a line
            // https://api.511.org/transit/patterns?api_key=ea43df0e-f500-4422-83a5-f00910b6b2d5&operator_id=SF&line_id=29&servicejourneypattern_id=221604
            // todo - data collection happens here
            val token: String = System.getenv("TransitToken")
            val response: HttpResponse = HttpClient(CIO) {
                install(ContentEncoding) {
                    gzip(0.9F)
                }
            }.use { client ->
                client.get("https://api.511.org") {
                    url {
                        appendPathSegments("transit", "StopMonitoring")
                        parameters.append("api_key", token)
                        parameters.append("agency", "SF")
                    }
                }
            }
            if (response.status.value in 200..299) {
                logger.info("Successful response at ${response.request.url}")
            }
            val body: ByteArray = response.body()
            processBody(body)
        }
    }

    private fun processBody(body: ByteArray) {
        logger.info(String(body))
        logger.info("completed data collection.")
    }
}