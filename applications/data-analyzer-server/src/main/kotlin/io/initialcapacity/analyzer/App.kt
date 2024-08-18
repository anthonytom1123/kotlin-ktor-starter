package io.initialcapacity.analyzer

import metrics.healthcheck.healthCheckModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.cors.routing.*
import java.util.*

fun Application.analyserModule(repository: TaskRepository = PostgresTaskRepository()) {
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
    configureSerialization(repository)
    configureDatabases()
    healthCheckModule(repository)
    configureRouting()
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8887
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = { analyserModule() }).start(wait = true)
}