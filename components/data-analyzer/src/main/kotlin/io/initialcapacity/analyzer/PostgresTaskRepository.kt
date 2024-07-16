package io.initialcapacity.analyzer

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

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

//    override suspend fun getTasksByPriority(priority: Priority): List<Task> = suspendTransaction {
//        TaskDAO
//            .find { (TaskTable.priority eq priority.toString()) }
//            .map(::daoToModel)
//    }
//
//    override suspend fun getTaskByName(name: String): Task? = suspendTransaction {
//        TaskDAO
//            .find { (TaskTable.name eq name) }
//            .limit(1)
//            .map(::daoToModel)
//            .firstOrNull()
//    }

    override suspend fun addTask(task: Task): Unit = suspendTransaction {
        TaskDAO.new {
            lineRef = task.lineRef
            lineName = task.lineName
            stopRef = task.stopRef
            stopName = task.stopName
            directionRef = task.directionRef
            occupancy = task.occupancy
            arrivalTime = task.arrivalTime
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
}