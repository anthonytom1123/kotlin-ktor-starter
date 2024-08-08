package io.initialcapacity.web

import freemarker.cache.ClassTemplateLoader
import io.initialcapacity.analyzer.Task
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.compression.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.response.*
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*
import kotlin.io.use

fun Application.module() {
    val logger = LoggerFactory.getLogger(this.javaClass)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(Routing) {
        get("/") {
            val busDataList = getAllTasks(logger)
            call.respond(FreeMarkerContent("index.ftl", mapOf("busDataList" to busDataList)))
        }
        get("/test") {
            call.respondText("Success!")
        }

        staticResources("/static/styles", "static/styles")
        staticResources("/static/images", "static/images")
        staticResources(
            "/static",
            "static"
        )
    }
}

private fun getAllTasks(logger: Logger): List<Task> =
    runBlocking {
        val url = System.getenv("ANALYZER_URL")
        logger.info("Accepted / path")
        val response: HttpResponse = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }.use { client ->
            logger.info("Attempting to connect to $url")
            client.get(url) {
                contentType(ContentType.Application.Json)
            }
        }

        val body: String = response.body()
        val busDataList: List<Task> = Json.decodeFromString(body)
        logger.info("Success! Fetched ${busDataList.size} task")
        return@runBlocking busDataList
}

private fun filterByLine(lineRef: String) {
    return
}

private fun filterByStop(stopRef: String) {
    return
}

private fun filterByDirection(directionRef: String) {
    return
}

private fun PipelineContext<Unit, ApplicationCall>.headers(): MutableMap<String, String> {
    val headers = mutableMapOf<String, String>()
    call.request.headers.entries().forEach { entry ->
        headers[entry.key] = entry.value.joinToString()
    }
    return headers
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8888
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = { module() }).start(wait = true)
}
