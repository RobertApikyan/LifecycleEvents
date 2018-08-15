package robertapikyan.com.events.executors

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