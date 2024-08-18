package io.initialcapacity.analyzer

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskByLineRef(ref: String): List<Task>
    suspend fun getTaskByLineName(name: String): List<Task>
    suspend fun getTaskByStopRef(ref: Int): List<Task>
    suspend fun getTaskByStopName(name: String): List<Task>
    suspend fun addTask(task: Task)
    suspend fun addMultipleTasks(taskList: MutableList<Task>)
    suspend fun removeTask(lineRef: String, stopRef: Int, directionRef: String, arrivalTime: String): Boolean
    suspend fun clearTasks(): Int
}