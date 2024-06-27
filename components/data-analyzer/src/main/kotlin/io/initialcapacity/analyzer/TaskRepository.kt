package io.initialcapacity.analyzer

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTasksByPriority(priority: Priority): List<Task>
    suspend fun getTaskByName(name: String): Task?
    suspend fun addTask(task: Task)
    suspend fun removeTask(name: String): Boolean
}