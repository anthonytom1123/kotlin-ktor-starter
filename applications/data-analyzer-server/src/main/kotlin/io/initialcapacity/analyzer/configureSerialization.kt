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
import org.jetbrains.exposed.sql.Except
import org.slf4j.Logger
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import java.time.Duration

fun Application.configureSerialization(repository: TaskRepository) {
    val logger = LoggerFactory.getLogger(this.javaClass)

    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/tasks") {
            get {
                logger.info("get /tasks received")
                var tasks = repository.getAllTasks()
                tasks = calculateRemainingTime(tasks, logger)
                call.respond(tasks)
                logger.info("get /tasks sent")
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
                    logger.info("post/list received")
                    repository.clearTasks()
                    val taskList = call.receive<MutableList<Task>>()
                    logger.info("received ${taskList.size} tasks")
                    repository.addMultipleTasks(taskList)
                    call.respond(HttpStatusCode.NoContent)
                } catch (e: IllegalStateException) {
                    logger.error("Illegal State Exception: ${e.message}")
                    call.respond(HttpStatusCode.BadRequest)
                } catch (e: JsonConvertException) {
                    logger.error("JsonConvertException: ${e.message}")
                    call.respond(HttpStatusCode.BadRequest)
                } catch (e: Exception) {
                    logger.error("Unexpected error: ${e.message}")
                    call.respond(HttpStatusCode.BadRequest)
                } finally {
                    logger.info("post/list finished")
                }
            }

            post("/single") {
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

private fun calculateRemainingTime(task: Task): Task {
    if(task.arrivalTime == "null") {
        return task
    }

    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val currentTime = getCurrentTime()
    val duration = Duration.between(currentTime, LocalDateTime.parse(task.arrivalTime.removeSurrounding("\"","\""), formatter))
    task.arrivalTime = duration.toMinutes().toString()
    return task
}

private fun calculateRemainingTime(taskList: List<Task>, logger: Logger): List<Task> {
    try {
        logger.info("calculating remaining time")
        taskList.map { task ->
            calculateRemainingTime(task)
        }
    } catch (e: Exception) {
        logger.error("Error calculating remaining time: $e")
        throw e
    }
    return taskList
}

private fun getCurrentTime(): LocalDateTime {
    val zoneId = ZoneId.of("UTC")
    return ZonedDateTime.now(zoneId).toLocalDateTime()
}