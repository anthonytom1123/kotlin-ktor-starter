package test.initialcapacity.collector

import io.initialcapacity.collector.ExampleTask
import io.initialcapacity.collector.ExampleWorkFinder
import io.initialcapacity.collector.ExampleWorker
import io.initialcapacity.collector.collectorModule
import io.initialcapacity.workflow.WorkScheduler
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.*
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.*
import org.junit.Test
import kotlin.test.*

class AppTest {
    private lateinit var testWorker: ExampleWorker

    @BeforeTest
    fun setUp() {
        testWorker = spyk(ExampleWorker())
        coEvery { testWorker.collectData(any()) } returns getByteList()
    }

    @Test
    fun testEmptyHome() = testApp {
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testRefresh() = testApp {
        val response = client.get("/refresh")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testSetBusyAndIsBusy() {
        testWorker.setBusy(true)
        assertTrue(testWorker.isBusy())
    }

    private fun testApp(block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) {
        testApplication {
            application { collectorModule(WorkScheduler<ExampleTask>(ExampleWorkFinder(), mutableListOf(ExampleWorker()), 65)) }
            block(client)
        }
    }

    private fun getByteList(): ByteArray {
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
                                "LineRef": 29,
                                "DirectionRef": "IB",
                                "FramedVehicleJourneyRef": {
                                  "DataFrameRef": "2024-08-16",
                                  "DatedVehicleJourneyRef": "11606592_M31"
                                },
                                "PublishedLineName": "SUNSET",
                                "OperatorRef": "SF",
                                "OriginRef": 14648,
                                "OriginName": "Fitzgerald Ave & Keith St",
                                "DestinationRef": 13491,
                                "DestinationName": "25th Ave & California St",
                                "Monitored": true,
                                "InCongestion": "",
                                "VehicleLocation": {
                                  "Longitude": -122.433487,
                                  "Latitude": 37.7219505
                                },
                                "Bearing": 300,
                                "Occupancy": "seatsAvailable",
                                "VehicleRef": 8733,
                                "MonitoredCall": {
                                  "StopPointRef": 15794,
                                  "StopPointName": "Ocean Ave & Lee St",
                                  "VehicleLocationAtStop": "",
                                  "VehicleAtStop": false,
                                  "DestinationDisplay": "25th & California",
                                  "AimedArrivalTime": "2024-08-17T06:22:22Z",
                                  "ExpectedArrivalTime": "2024-08-17T06:23:04Z",
                                  "AimedDepartureTime": "2024-08-17T06:22:22Z",
                                  "ExpectedDepartureTime": "",
                                  "Distances": ""
                                }
                              }
                            },
                            {
                              "RecordedAtTime": "2024-08-17T06:17:24Z",
                              "MonitoringRef": 15794,
                              "MonitoredVehicleJourney": {
                                "LineRef": "K",
                                "DirectionRef": "IB",
                                "FramedVehicleJourneyRef": {
                                  "DataFrameRef": "2024-08-16",
                                  "DatedVehicleJourneyRef": "11621097_M31"
                                },
                                "PublishedLineName": "INGLESIDE",
                                "OperatorRef": "SF",
                                "OriginRef": 17778,
                                "OriginName": "San Jose Ave & Geneva Ave",
                                "DestinationRef": 16992,
                                "DestinationName": "Metro Embarcadero Station",
                                "Monitored": true,
                                "InCongestion": "",
                                "VehicleLocation": {
                                  "Longitude": -122.447121,
                                  "Latitude": 37.7212105
                                },
                                "Bearing": 315,
                                "Occupancy": "seatsAvailable",
                                "VehicleRef": 2048,
                                "MonitoredCall": {
                                  "StopPointRef": 15794,
                                  "StopPointName": "Ocean Ave & Lee St",
                                  "VehicleLocationAtStop": "",
                                  "VehicleAtStop": false,
                                  "DestinationDisplay": "Embarcadero Station",
                                  "AimedArrivalTime": "2024-08-17T06:22:58Z",
                                  "ExpectedArrivalTime": "2024-08-17T06:23:40Z",
                                  "AimedDepartureTime": "2024-08-17T06:22:58Z",
                                  "ExpectedDepartureTime": "",
                                  "Distances": ""
                                }
                              }
                            },
                            {
                              "RecordedAtTime": "2024-08-17T06:17:24Z",
                              "MonitoringRef": 15794,
                              "MonitoredVehicleJourney": {
                                "LineRef": "K",
                                "DirectionRef": "IB",
                                "FramedVehicleJourneyRef": {
                                  "DataFrameRef": "2024-08-16",
                                  "DatedVehicleJourneyRef": "11621096_M31"
                                },
                                "PublishedLineName": "INGLESIDE",
                                "OperatorRef": "SF",
                                "OriginRef": 17778,
                                "OriginName": "San Jose Ave & Geneva Ave",
                                "DestinationRef": 14509,
                                "DestinationName": "The Embarcadero & Folsom St",
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
                                  "DestinationDisplay": "Embarcadero Station",
                                  "AimedArrivalTime": "2024-08-17T06:42:58Z",
                                  "ExpectedArrivalTime": "2024-08-17T06:43:26Z",
                                  "AimedDepartureTime": "2024-08-17T06:42:58Z",
                                  "ExpectedDepartureTime": "",
                                  "Distances": ""
                                }
                              }
                            },
                            {
                              "RecordedAtTime": "2024-08-17T06:17:24Z",
                              "MonitoringRef": 15794,
                              "MonitoredVehicleJourney": {
                                "LineRef": 29,
                                "DirectionRef": "IB",
                                "FramedVehicleJourneyRef": {
                                  "DataFrameRef": "2024-08-16",
                                  "DatedVehicleJourneyRef": "11606593_M31"
                                },
                                "PublishedLineName": "SUNSET",
                                "OperatorRef": "SF",
                                "OriginRef": 14648,
                                "OriginName": "Fitzgerald Ave & Keith St",
                                "DestinationRef": 13491,
                                "DestinationName": "25th Ave & California St",
                                "Monitored": true,
                                "InCongestion": "",
                                "VehicleLocation": {
                                  "Longitude": -122.394653,
                                  "Latitude": 37.7227898
                                },
                                "Bearing": 120,
                                "Occupancy": "seatsAvailable",
                                "VehicleRef": 8738,
                                "MonitoredCall": {
                                  "StopPointRef": 15794,
                                  "StopPointName": "Ocean Ave & Lee St",
                                  "VehicleLocationAtStop": "",
                                  "VehicleAtStop": false,
                                  "DestinationDisplay": "25th & California",
                                  "AimedArrivalTime": "2024-08-17T06:42:22Z",
                                  "ExpectedArrivalTime": "2024-08-17T06:44:50Z",
                                  "AimedDepartureTime": "2024-08-17T06:42:22Z",
                                  "ExpectedDepartureTime": "",
                                  "Distances": ""
                                }
                              }
                            },
                            {
                              "RecordedAtTime": "2024-08-17T06:17:24Z",
                              "MonitoringRef": 15794,
                              "MonitoredVehicleJourney": {
                                "LineRef": "KBUS",
                                "DirectionRef": "IB",
                                "FramedVehicleJourneyRef": {
                                  "DataFrameRef": "2024-08-16",
                                  "DatedVehicleJourneyRef": "11621764_M31"
                                },
                                "PublishedLineName": "K INGLESIDE BUS",
                                "OperatorRef": "SF",
                                "OriginRef": 16260,
                                "OriginName": "San Jose Ave & Geneva Ave",
                                "DestinationRef": 16497,
                                "DestinationName": "Steuart St & Mission St",
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
                                  "DestinationDisplay": "Embarcadero",
                                  "AimedArrivalTime": "2024-08-17T07:04:11Z",
                                  "ExpectedArrivalTime": "2024-08-17T07:04:00Z",
                                  "AimedDepartureTime": "2024-08-17T07:04:11Z",
                                  "ExpectedDepartureTime": "",
                                  "Distances": ""
                                }
                              }
                            },
                            {
                              "RecordedAtTime": "2024-08-17T06:17:24Z",
                              "MonitoringRef": 15794,
                              "MonitoredVehicleJourney": {
                                "LineRef": 29,
                                "DirectionRef": "IB",
                                "FramedVehicleJourneyRef": {
                                  "DataFrameRef": "2024-08-16",
                                  "DatedVehicleJourneyRef": "11606594_M31"
                                },
                                "PublishedLineName": "SUNSET",
                                "OperatorRef": "SF",
                                "OriginRef": 14648,
                                "OriginName": "Fitzgerald Ave & Keith St",
                                "DestinationRef": 13491,
                                "DestinationName": "25th Ave & California St",
                                "Monitored": true,
                                "InCongestion": "",
                                "VehicleLocation": {
                                  "Longitude": -122.394775,
                                  "Latitude": 37.7228889
                                },
                                "Bearing": 120,
                                "Occupancy": "seatsAvailable",
                                "VehicleRef": 8620,
                                "MonitoredCall": {
                                  "StopPointRef": 15794,
                                  "StopPointName": "Ocean Ave & Lee St",
                                  "VehicleLocationAtStop": "",
                                  "VehicleAtStop": false,
                                  "DestinationDisplay": "25th & California",
                                  "AimedArrivalTime": "2024-08-17T07:02:22Z",
                                  "ExpectedArrivalTime": "2024-08-17T07:04:25Z",
                                  "AimedDepartureTime": "2024-08-17T07:02:22Z",
                                  "ExpectedDepartureTime": "",
                                  "Distances": ""
                                }
                              }
                            },
                            {
                              "RecordedAtTime": "2024-08-17T06:17:24Z",
                              "MonitoringRef": 15794,
                              "MonitoredVehicleJourney": {
                                "LineRef": 91,
                                "DirectionRef": "OB",
                                "FramedVehicleJourneyRef": {
                                  "DataFrameRef": "2024-08-16",
                                  "DatedVehicleJourneyRef": "11618125_M31"
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
                                  "Longitude": -122.396439,
                                  "Latitude": 37.7759399
                                },
                                "Bearing": 30,
                                "Occupancy": "seatsAvailable",
                                "VehicleRef": 8832,
                                "MonitoredCall": {
                                  "StopPointRef": 15794,
                                  "StopPointName": "Ocean Ave & Lee St",
                                  "VehicleLocationAtStop": "",
                                  "VehicleAtStop": false,
                                  "DestinationDisplay": "West Portal Station",
                                  "AimedArrivalTime": "2024-08-17T07:26:40Z",
                                  "ExpectedArrivalTime": "2024-08-17T07:34:56Z",
                                  "AimedDepartureTime": "2024-08-17T07:26:40Z",
                                  "ExpectedDepartureTime": "",
                                  "Distances": ""
                                }
                              }
                            },
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