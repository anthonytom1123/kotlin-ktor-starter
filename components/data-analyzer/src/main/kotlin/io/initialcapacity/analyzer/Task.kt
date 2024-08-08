package io.initialcapacity.analyzer

import kotlinx.serialization.Serializable


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
