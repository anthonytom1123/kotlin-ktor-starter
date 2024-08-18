package io.initialcapacity.analyzer

import org.slf4j.LoggerFactory

class FakeTaskRepository: TaskRepository {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val tasks = mutableListOf(
        Task("250", "Line 250", 12345, "Test Stop 1", "OB", "seatsAvailable", "2024-07-02T05:21:52Z"),
        Task("251", "Line 251", 54321, "Test Stop 2", "IB", "standingAvailable", "2024-07-02T05:22:54Z")
    )

    override suspend fun getAllTasks(): List<Task> = tasks

    override suspend fun getTaskByLineRef(ref: String): List<Task> {
        return tasks.filter {
            it.lineRef == ref
        }
    }

    override suspend fun getTaskByLineName(name: String): List<Task> {
        logger.info("FakeTaskRepository.getTaskByLineName: $tasks")
        return tasks.filter {
            logger.info("FakeTaskRepository.getTaskByLineName: ${it.lineName} == $name is ${it.lineName == name}")
            it.lineName == name
        }
    }

    override suspend fun getTaskByStopRef(ref: Int): List<Task> {
        return tasks.filter {
            it.stopRef == ref
        }
    }

    override suspend fun getTaskByStopName(name: String): List<Task> {
        return tasks.filter {
            it.stopName == name
        }
    }

    override suspend fun addTask(task: Task) {
        tasks.add(task)
    }

    override suspend fun removeTask(lineRef: String, stopRef: Int, directionRef: String, arrivalTime: String): Boolean {
        return tasks.removeIf {
            it.lineRef == lineRef &&
            it.stopRef == stopRef &&
            it.directionRef == directionRef
            it.arrivalTime == arrivalTime
        }
    }

    override suspend fun clearTasks(): Int {
        if(tasks.removeAll(tasks)) {
            return 1
        }
        return 0

    }

    override suspend fun addMultipleTasks(taskList: MutableList<Task>) {
        taskList.forEach { task ->
            tasks.add(task)
        }
    }

}