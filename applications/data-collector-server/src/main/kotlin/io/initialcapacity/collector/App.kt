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

fun Application.collectorModule(scheduler: WorkScheduler<ExampleTask>) {
    val logger = LoggerFactory.getLogger(this.javaClass)
    if(pluginOrNull(CORS) == null) {
        install(CORS) {
            anyHost()
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Post)
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.AccessControlAllowOrigin)
            allowHeader(HttpHeaders.Accept)
        }
    }
    install(Routing) {
        get("/") {
            call.respondText("working")
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
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = { collectorModule(WorkScheduler<ExampleTask>(ExampleWorkFinder(), mutableListOf(ExampleWorker()), 65)) }).start(wait = true)
}