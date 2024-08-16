package io.initialcapacity.workflow

import org.slf4j.LoggerFactory

class NoopWorker(override val name: String = "noop-worker") : Worker<NoopTask> {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    @Volatile
    private var busy = false

    override fun isBusy(): Boolean {
        return busy
    }
    override fun setBusy(v: Boolean) {
        busy = v
    }

    override fun execute(task: NoopTask) {
        logger.info("doing work. {} {}", task.name, task.value)
    }
}