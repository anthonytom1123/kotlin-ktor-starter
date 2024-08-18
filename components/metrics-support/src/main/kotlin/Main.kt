package metrics.healthcheck

import io.initialcapacity.analyzer.TaskRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.client.statement.HttpResponse
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

fun Application.healthCheckModule(database: TaskRepository) {
    routing {
        get("/test") {
            call.respondText("working")
        }
        get("/healthCheck") {
            val dbStatus = checkDatabaseStatus(database)
            val apiStatus = check511Status()
            if(dbStatus && apiStatus) {
                call.respond(HttpStatusCode.OK, "Healthy")
            } else {
                call.respond(HttpStatusCode.ServiceUnavailable, "Not Healthy")
            }

        }
    }
}

suspend fun checkDatabaseStatus(database: TaskRepository): Boolean {
    val dataList = database.getAllTasks()
    return dataList.isNotEmpty()
}

fun check511Status(): Boolean = runBlocking {
    try {
        val response: HttpResponse = HttpClient(CIO) {
            install(ContentEncoding) {
                gzip(0.9F)
            }
            install(ContentNegotiation) {
                json()
            }
        }.use { client ->
            client.get("https://511.org/") {}
        }

        if(response.status.value in 200..299) {
            return@runBlocking true
        }
        return@runBlocking false
    } catch (e: Exception) {
        return@runBlocking false
    }
}