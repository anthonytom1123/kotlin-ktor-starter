package io.initialcapacity.workflow

interface Worker<T> {
    val name: String
    fun isBusy(): Boolean
    fun setBusy(v: Boolean)
    fun execute(task: T)
}