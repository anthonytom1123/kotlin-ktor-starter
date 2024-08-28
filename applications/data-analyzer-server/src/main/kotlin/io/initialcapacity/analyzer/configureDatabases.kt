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

fun Application.configureDatabases() {
    val logger = LoggerFactory.getLogger(this.javaClass)
    val dbUrl = System.getenv("DB_URL")?: throw IllegalAccessException("DB_URL environment not set.")
    val user= System.getenv("DB_USER")?: throw IllegalArgumentException("DB_USER environment not set.")
    val password = System.getenv("DB_PASSWORD")?: throw IllegalStateException("DB_PASSWORD environment not set.")
    logger.info("dbUrl: $dbUrl, user: $user, pass: $password")
    Database.connect(
        url = dbUrl,
        user = user,
        password = password,
    )
}