package test.healthCheck

import io.initialcapacity.analyzer.FakeTaskRepository
import io.initialcapacity.analyzer.TaskRepository
import io.initialcapacity.analyzer.*
import org.junit.Test
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.*
import kotlinx.coroutines.runBlocking
import metrics.healthcheck.checkDatabaseStatus
import metrics.healthcheck.healthCheckModule
import kotlin.test.*

class AppTest {
    private val mockDatabase = mockk<TaskRepository>()

    @Test
    fun testRouting() = testApp{
        val response = client.get("/test")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("working", response.bodyAsText())
    }

    @Test
    fun testCheckDatabaseStatus() = runBlocking {
        every { runBlocking {mockDatabase.getAllTasks()} } returns listOf(
            Task(
                "250",
                "Line 250",
                99999,
                "Test Stop 1",
                "IB",
                "seatsAvailable",
                "2024-07-02T05:21:58Z"
            )
        )
        val result = checkDatabaseStatus(mockDatabase)
        assertTrue(result)
    }

    @Test
    fun testCheckDatabaseStatusReturnsFalseWhenEmpty() = runBlocking {
        every { runBlocking {mockDatabase.getAllTasks()} } returns listOf()
        val result = checkDatabaseStatus(mockDatabase)
        assertFalse(result)
    }

    private fun testApp(block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit) {
        testApplication {
            application {
                healthCheckModule(FakeTaskRepository())
            }
            block(client)
        }
    }
}
