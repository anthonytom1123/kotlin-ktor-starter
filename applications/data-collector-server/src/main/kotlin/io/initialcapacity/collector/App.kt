package io.initialcapacity.collector

import io.initialcapacity.workflow.WorkScheduler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.slf4j.LoggerFactory
import java.util.*

fun Application.module() {
    val logger = LoggerFactory.getLogger(this.javaClass)
    val scheduler = WorkScheduler<ExampleTask>(ExampleWorkFinder(), mutableListOf(ExampleWorker()), 65)
    install(CORS){
        anyHost()
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowHeader(io.ktor.http.HttpHeaders.ContentType)
        allowHeader(io.ktor.http.HttpHeaders.AccessControlAllowOrigin)
        allowHeader(io.ktor.http.HttpHeaders.Accept)
    }
    install(Routing) {
        get("/") {
            call.respondText("hi!", ContentType.Text.Html)
        }

        get("/refresh") {
            logger.info("got refresh call")
            scheduler.addWork()
            call.respond(HttpStatusCode.OK)
        }
        scheduler.start()
    }

    environment.monitor.subscribe(ApplicationStopping) {
        try {
            scheduler.shutdown()
        } catch(e: Exception) {
            logger.error("Error shutting down: $e")
        }
    }
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8886
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = { module() }).start(wait = true)
}