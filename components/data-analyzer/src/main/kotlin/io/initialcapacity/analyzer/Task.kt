package io.initialcapacity.analyzer

import kotlinx.serialization.Serializable

enum class Occupancy(val description: String) {
    Full("Full"),
    SeatsAvailable("Seats Available"),
    StandingAvailable("Standing Available")
}

enum class Direction {
    Inbound, Outbound
}

@Serializable
data class Task(
    val lineRef: String,
    val lineName: String,
    val stopRef: Int,
    val stopName: String,
    val directionRef: String,
    val occupancy: String,
    var arrivalTime: String
)
