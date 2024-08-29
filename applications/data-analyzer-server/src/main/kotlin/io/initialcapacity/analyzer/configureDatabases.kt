package io.initialcapacity.analyzer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.sql.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.slf4j.LoggerFactory
import java.net.URI

fun Application.configureDatabases() {
    val logger = LoggerFactory.getLogger(this.javaClass)
    val dbUrl = System.getenv("DB_URL")?: throw IllegalAccessException("DB_URL environment not set.")
    val dbUri = URI(dbUrl)
    val jdbcUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}?user=${dbUri.userInfo.split(":")[0]}&password=${dbUri.userInfo.split(":")[1]}"
    Database.connect(
        url = jdbcUrl,
        driver = "org.postgresql.Driver",
    )
}