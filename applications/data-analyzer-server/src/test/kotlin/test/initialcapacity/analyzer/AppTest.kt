package test.initialcapacity.analyzer

import io.initialcapacity.analyzer.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testGetAllTasks() = testApplication {
        application {
            val repository = FakeTaskRepository()
            configureSerialization(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/tasks") {
            accept(ContentType.Application.Json)
        }
        val results = response.body<List<Task>>()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(2, results.size)

        val expectedTaskRef = listOf("250","251")
        val actualTaskRef = results.map(Task::lineRef)
        assertContentEquals(expectedTaskRef, actualTaskRef)
        client.close()
    }

    @Test
    fun testGetTasksByLineRef() = testApplication {
        application {
            val repository = FakeTaskRepository()
            configureSerialization(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val task = Task(
            "250",
            "Line 250",
            99999,
            "Test Stop 1",
            "IB",
            "seatsAvailable",
            "2024-07-02T05:21:58Z"
        )

        client.post("/tasks/single") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )
            setBody(task)
        }

        val testResponse = client.get("/tasks/byLineRef/250") {
            accept(ContentType.Application.Json)
        }
        val results = testResponse.body<List<Task>>()

        assertEquals(HttpStatusCode.OK, testResponse.status)

        val expectedTaskName = "Line 250"
        val actualTaskNames = results.map(Task::lineName)
        assertContains(actualTaskNames, expectedTaskName)
        client.close()
    }

    @Test
    fun testGetTasksByLineName() = testApplication {
        application {
            val repository = FakeTaskRepository()
            configureSerialization(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/tasks/byLineName/Line 250") {
            accept(ContentType.Application.Json)
        }
        val results = response.body<List<Task>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedTaskNames = listOf("Line 250")
        val actualTaskNames = results.map(Task::lineName)
        assertContentEquals(expectedTaskNames, actualTaskNames)
        client.close()
    }

    @Test
    fun testGetTasksByStopRef() = testApplication {
        application {
            val repository = FakeTaskRepository()
            configureSerialization(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/tasks/byStopRef/12345") {
            accept(ContentType.Application.Json)
        }
        val results = response.body<List<Task>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedTaskNames = listOf("Line 250")
        val actualTaskNames = results.map(Task::lineName)
        assertContentEquals(expectedTaskNames, actualTaskNames)
        client.close()
    }

    @Test
    fun testGetTasksByStopName() = testApplication {
        application {
            val repository = FakeTaskRepository()
            configureSerialization(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/tasks/byStopName/Test Stop 1") {
            accept(ContentType.Application.Json)
        }
        val results = response.body<List<Task>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedTaskNames = listOf("Line 250")
        val actualTaskNames = results.map(Task::lineName)
        assertContentEquals(expectedTaskNames, actualTaskNames)
        client.close()
    }

    @Test
    fun testPostgresAddingTasks() = testApplication {
        application {
            analyserModule()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val task = Task(
            "252",
            "Line 252",
            99999,
            "Test Stop 3",
            "IB",
            "seatsAvailable",
            "2024-07-02T05:21:58Z"
        )
        val response1 = client.post("/tasks/single") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )
            setBody(task)
        }
        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response2 = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response2.status)

        val taskNames = response2
            .body<List<Task>>()
            .map { it.lineName }

        assertContains(taskNames, "Line 252")
        client.close()
    }

    @Test
    fun testPostgresGetAllTasks() = testApplication {
        application {
            analyserModule()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val task = Task(
            "256",
            "Line 256",
            99998,
            "Test Stop 6",
            "OB",
            "full",
            "2024-07-02T05:22:58Z"
        )

        val response1 = client.post("/tasks/single") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )
            setBody(task)
        }
        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response = client.get("/tasks") {
            accept(ContentType.Application.Json)
        }
        val results = response.body<List<Task>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val actualLineRef = results.map(Task::lineRef)
        assertContains(actualLineRef, "256")
        client.close()
    }
}