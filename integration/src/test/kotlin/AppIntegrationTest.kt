package test.integration

import io.initialcapacity.analyzer.*
import io.initialcapacity.analyzer.ExampleWorkFinder
import io.initialcapacity.analyzer.configureRouting
import io.initialcapacity.collector.*
import io.initialcapacity.collector.ExampleTask
import io.initialcapacity.collector.ExampleWorker
import io.initialcapacity.workflow.WorkScheduler
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.mockk.*

class AppIntegrationTest {
    private lateinit var mockRepository: TaskRepository
    private lateinit var worker: ExampleWorker

    @BeforeTest
    fun setup() {
        mockRepository = FakeTaskRepository()
        worker = spyk(ExampleWorker())
        every { worker.collectData("") } returns getTestData()
    }

    @Test
    fun testAnalyzerEndpoint() = testApplication {
        application {
            configureRouting()
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("working", bodyAsText())
        }
    }

    @Test
    fun testCollectorEndpoint() = testApplication {
        application {
            val mockScheduler = WorkScheduler<ExampleTask>(io.initialcapacity.collector.ExampleWorkFinder(), mutableListOf(worker), 65)
            collectorModule(mockScheduler)
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("working", bodyAsText())
        }
    }

    @Test
    fun testCollectorDataIsSavedByAnalyser() = testApplication {

        application {
            val mockScheduler = WorkScheduler<ExampleTask>(io.initialcapacity.collector.ExampleWorkFinder(), mutableListOf(worker), 65)
            collectorModule(mockScheduler)
            analyserModule(mockRepository, false)
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.get("/refresh") {}.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        client.get("/tasks") {}.apply {
            assertEquals(HttpStatusCode.OK, status)
            val response: List<Task> = body()
            assertTrue(response.isNotEmpty())
        }
    }

    private fun getTestData(): ByteArray {
        return """{
                  "ServiceDelivery": {
                    "ResponseTimestamp": "2024-08-17T06:17:32Z",
                    "ProducerRef": "SF",
                    "Status": true,
                    "StopMonitoringDelivery": {
                      "ResponseTimestamp": "2024-08-17T06:17:32Z",
                      "Status": true,
                      "MonitoredStopVisit": [
                        {
                          "RecordedAtTime": "2024-08-17T06:17:24Z",
                          "MonitoringRef": 15794,
                          "MonitoredVehicleJourney": {
                            "LineRef": 91,
                            "DirectionRef": "OB",
                            "FramedVehicleJourneyRef": {
                              "DataFrameRef": "2024-08-16",
                              "DatedVehicleJourneyRef": "11618124_M31"
                            },
                            "PublishedLineName": "3RD-19TH AVE OWL",
                            "OperatorRef": "SF",
                            "OriginRef": 13164,
                            "OriginName": "4th St & Townsend St",
                            "DestinationRef": 17058,
                            "DestinationName": "West Portal Ave & Ulloa St",
                            "Monitored": true,
                            "InCongestion": "",
                            "VehicleLocation": {
                              "Longitude": "",
                              "Latitude": ""
                            },
                            "Bearing": "",
                            "Occupancy": "",
                            "VehicleRef": "",
                            "MonitoredCall": {
                              "StopPointRef": 15794,
                              "StopPointName": "Ocean Ave & Lee St",
                              "VehicleLocationAtStop": "",
                              "VehicleAtStop": "",
                              "DestinationDisplay": "West Portal Station",
                              "AimedArrivalTime": "2024-08-17T07:56:40Z",
                              "ExpectedArrivalTime": "2024-08-17T08:06:21Z",
                              "AimedDepartureTime": "2024-08-17T07:56:40Z",
                              "ExpectedDepartureTime": "",
                              "Distances": ""
                            }
                          }
                        }
                      ]
                    }
                  }
                }""".toByteArray()
    }
}