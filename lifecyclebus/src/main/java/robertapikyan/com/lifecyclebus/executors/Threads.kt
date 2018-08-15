package robertapikyan.com.lifecyclebus.executors

/*
 * Created by Robert Apikyan on 8/15/2018.
 */

enum class Threads {
    BACKGROUND,
    MAIN;

    internal fun execute(block: () -> Unit) {
        when (this) {
            MAIN -> TaskExecutors.executeOnMainThread(block)
            BACKGROUND -> TaskExecutors.executeOnBackgroundThread(block)
        }
    }
}