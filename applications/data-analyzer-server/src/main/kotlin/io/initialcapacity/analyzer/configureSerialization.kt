package io.initialcapacity.analyzer

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import org.slf4j.LoggerFactory

fun Application.configureSerialization(repository: TaskRepository) {
    val logger = LoggerFactory.getLogger(this.javaClass)

    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/tasks") {
            get {
                val tasks = repository.getAllTasks()
                call.respond(tasks)
            }

            get("/byLineRef/{lineRef}") {
                logger.info("get /tasks/byLineRef received")
                val ref = call.parameters["lineRef"]
                if (ref == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val task = repository.getTaskByLineRef(ref)
                if (task.isEmpty()) {
                    logger.info("get/byLineRef/: [$ref] was not found.")
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(HttpStatusCode.OK, task)
            }

            get("/byLineName/{taskName}") {
                logger.info("get /tasks/byLineName received")
                val name = call.parameters["taskName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val task = repository.getTaskByLineName(name)
                if (task.isEmpty()) {
                    logger.info("get/byLineName/: [$name] was not found.")
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(HttpStatusCode.OK, task)
            }

            get("/byStopRef/{stopRef}") {
                logger.info("get /tasks/byStopRef received")
                val ref = call.parameters["stopRef"]
                if (ref == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val task = repository.getTaskByStopRef(ref.toInt())
                if (task.isEmpty()) {
                    logger.info("get/byStopRef/: [$ref] was not found.")
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(HttpStatusCode.OK, task)
            }

            get("/byStopName/{stopName}") {
                logger.info("get /tasks/byStopName received")
                val name = call.parameters["stopName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val task = repository.getTaskByStopName(name)
                if (task.isEmpty()) {
                    logger.info("get/byStopName/: [$name] was not found.")
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(HttpStatusCode.OK, task)
            }

            post {
                try {
                    logger.info("post received")
                    val task = call.receive<Task>()
                    repository.addTask(task)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post("/list") {
                try {
                    logger.info("post/list received")
                    val taskList = call.receive<MutableList<Task>>()
                    taskList.forEach {
                        repository.addTask(it)
                    }
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/delete?{stopRef}&{lineRef}&{directionRef}&{arrivalTime}") {
                val stopRef = call.parameters["stopRef"]
                val lineRef: Int? = call.parameters["lineRef"]?.toInt()
                val directionRef = call.parameters["directionRef"]
                val arrivalTime = call.parameters["arrivalTime"]
                if (stopRef == null || lineRef == null || directionRef == null || arrivalTime == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.removeTask(stopRef, lineRef, directionRef, arrivalTime)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}