package io.initialcapacity.analyzer

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object TaskTable : IntIdTable("task") {
    val lineRef = varchar("lineRef", 10)
    val lineName = varchar("lineName", 50)
    val stopRef = integer("stopRef")
    val stopName = varchar("stopName", 50)
    val directionRef = varchar("directionRef", 10)
    //seatsAvailable, standingAvailable
    val occupancy = varchar("occupancy", 20)
    val arrivalTime = varchar("arrivalTime", 30)
}

class TaskDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TaskDAO>(TaskTable)

    var lineRef by TaskTable.lineRef
    var lineName by TaskTable.lineName
    var stopRef by TaskTable.stopRef
    var stopName by TaskTable.stopName
    var directionRef by TaskTable.directionRef
    var occupancy by TaskTable.occupancy
    var arrivalTime by TaskTable.arrivalTime
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)


fun daoToModel(dao: TaskDAO) = Task(
    dao.lineRef,
    dao.lineName,
    dao.stopRef,
    dao.stopName,
    dao.directionRef,
    dao.occupancy,
    dao.arrivalTime
)