package io.initialcapacity.analyzer

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.deleteAll

class PostgresTaskRepository : TaskRepository {
    override suspend fun getAllTasks(): List<Task> = suspendTransaction {
        TaskDAO.all().map(::daoToModel)
    }

    override suspend fun getTaskByLineRef(ref: String): List<Task> = suspendTransaction {
        TaskDAO.find { (TaskTable.lineRef eq ref) }.map(::daoToModel)
    }

    override suspend fun getTaskByLineName(name: String): List<Task> = suspendTransaction {
        TaskDAO.find { (TaskTable.lineName eq name) }.map(::daoToModel)
    }

    override suspend fun getTaskByStopRef(ref: Int): List<Task> = suspendTransaction {
        TaskDAO.find { (TaskTable.stopRef eq ref) }.map(::daoToModel)
    }

    override suspend fun getTaskByStopName(name: String): List<Task> = suspendTransaction {
        TaskDAO.find { (TaskTable.stopName eq name) }.map(::daoToModel)
    }

    override suspend fun addTask(task: Task): Unit = suspendTransaction {
        try {
            TaskDAO.new {
                lineRef = task.lineRef.removeSurrounding("\"","\"")
                lineName = task.lineName.removeSurrounding("\"","\"")
                stopRef = task.stopRef
                stopName = task.stopName.removeSurrounding("\"","\"")
                directionRef = processDirection(task.directionRef.removeSurrounding("\"","\""))
                occupancy = processOccupancy(task.occupancy.removeSurrounding("\"","\""))
                arrivalTime = task.arrivalTime.removeSurrounding("\"","\"")
            }
        }
        catch(e: Exception) {
            println("$e")
            throw(e)
        }
    }

    override suspend fun removeTask(lineRef: String, stopRef: Int, directionRef: String, arrivalTime: String): Boolean = suspendTransaction {
        val rowsDeleted = TaskTable.deleteWhere {
            TaskTable.stopRef eq stopRef
            TaskTable.lineRef eq lineRef
            TaskTable.directionRef eq directionRef
            TaskTable.arrivalTime eq arrivalTime
        }
        rowsDeleted == 1
    }

    override suspend fun clearTasks(): Int = suspendTransaction {
        TaskTable.deleteAll()
    }

    private fun processOccupancy(occupancy: String): String {
        return when(occupancy) {
            "full" -> Occupancy.Full.description
            "seatsAvailable" -> Occupancy.SeatsAvailable.description
            "standingAvailable" -> Occupancy.StandingAvailable.description
            else -> "Data Unavailable"
        }
    }

    private fun processDirection(direction: String): String {
        return when(direction) {
            "IB" -> Direction.Inbound.toString()
            "OB" -> Direction.Outbound.toString()
            else -> "Data Unavailable"
        }
    }
}