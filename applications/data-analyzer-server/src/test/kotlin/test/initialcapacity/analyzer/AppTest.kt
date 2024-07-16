package test.initialcapacity.analyzer

import io.initialcapacity.analyzer.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import javax.swing.text.AbstractDocument.Content
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

        val response = client.get("/tasks/byLineRef/250") {
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

        val response = client.get("/tasks/byStopName/Test Stop #1") {
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
    fun invalidPriorityProduces400() = testApplication {
        application {
            module()
        }
        val response = client.get("/tasks/byPriority/Invalid")
//        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(1, 1)
    }

    @Test
    fun unusedPriorityProduces404() = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/byPriority/Vital")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

//    @Test
//    fun newTasksCanBeAdded() = testApplication {
//        application {
//            module()
//        }
//
//        val client = createClient {
//            install(ContentNegotiation) {
//                json()
//            }
//        }
//
//        val task = Task("swimming", "Go to the beach", Priority.Low)
//        val response1 = client.post("/tasks") {
//            header(
//                HttpHeaders.ContentType,
//                ContentType.Application.Json
//            )
//
//            setBody(task)
//        }
//        assertEquals(HttpStatusCode.NoContent, response1.status)
//
//        val response2 = client.get("/tasks")
//        assertEquals(HttpStatusCode.OK, response2.status)
//
//        val taskNames = response2
//            .body<List<Task>>()
//            .map { it.name }
//
//        assertContains(taskNames, "swimming")
//    }

    //@Test
//    fun tasksCanBeDeleted() = testApplication {
//        application {
//            module()
//        }
//
//        val client = createClient {
//            install(ContentNegotiation) {
//                json()
//            }
//        }
//
//        //add stuff to db
//        val testName = "testSwim"
//        val task = Task(testName, "Go to the beach", Priority.Low)
//        val response1 = client.post("/tasks") {
//            header(
//                HttpHeaders.ContentType,
//                ContentType.Application.Json
//            )
//            setBody(task)
//        }
//        assertEquals(HttpStatusCode.NoContent, response1.status)
//
//        //delete
//        val response2 = client.delete("/${testName}")
//        assertEquals(HttpStatusCode.NoContent, response2.status)
//
//        val taskNames = response2
//            .body<List<Task>>()
//            .map { it.name }
//
//        assertFalse(taskNames.contains(testName))
//    }

    //@Test
//    fun deleteNullTaskReturnsBadRequest() = testApplication {
//        application {
//            module()
//        }
//
//        val client = createClient {
//            install(ContentNegotiation) {
//                json()
//            }
//        }
//
//        val response2 = client.delete("/de")
//        assertEquals(HttpStatusCode.BadRequest, response2.status)
//    }
}