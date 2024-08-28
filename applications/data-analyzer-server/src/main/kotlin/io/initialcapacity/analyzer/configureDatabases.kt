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

fun Application.configureDatabases() {
    Database.connect(
        System.getenv("DATABASE_URL")?: throw IllegalAccessException("DB_URL environment not set."),
        user = System.getenv("DB_USER")?: throw IllegalArgumentException("DB_USER environment not set."),
        password = System.getenv("DB_PASSWORD")?: throw IllegalStateException("DB_PASSWORD environment not set."),
    )
}