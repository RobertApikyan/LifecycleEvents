package robertapikyan.com.lifecyclebus.executors

/*
 * Created by Robert Apikyan on 8/14/2018.
 */

abstract class TaskExecutor {
    abstract fun executeOnBackgroundThread(runnable: Runnable)

    abstract fun executeOnMainThread(runnable: Runnable)

    abstract fun isMainThread():Boolean
}