package robertapikyan.com.events.executors

abstract class TaskExecutor {
    abstract fun executeOnBackgroundThread(runnable: Runnable)

    abstract fun executeOnMainThread(runnable: Runnable)

    abstract fun isMainThread(): Boolean
}