package robertapikyan.com.events.executors

class TaskExecutors : TaskExecutor() {

    companion object {
        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { TaskExecutors() }

        fun executeOnBackgroundThread(runnable: () -> Unit) =
                instance.executeOnBackgroundThread(Runnable(runnable))

        fun executeOnMainThread(runnable: () -> Unit) =
                instance.executeOnMainThread(Runnable(runnable))

    }

    private var executor: TaskExecutor

    private val defaultExecutor: TaskExecutor = DefaultExecutor()

    init {
        executor = defaultExecutor
    }

    fun setTaskExecutor(executor: TaskExecutor) {
        this.executor = executor
    }

    override fun executeOnBackgroundThread(runnable: Runnable) =
            executor.executeOnBackgroundThread(runnable)

    override fun executeOnMainThread(runnable: Runnable) =
            executor.executeOnMainThread(runnable)

    override fun isMainThread() =
            executor.isMainThread()
}