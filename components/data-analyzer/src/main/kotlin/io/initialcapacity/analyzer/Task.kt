package io.initialcapacity.analyzer

import kotlinx.serialization.Serializable

enum class Priority {
    Low, Medium, High, Vital
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
