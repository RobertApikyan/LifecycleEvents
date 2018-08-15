package robertapikyan.com.events.executors

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

class DefaultExecutor : TaskExecutor() {

    private companion object {
        const val POOL_SIZE = 3
    }

    private val executorService = Executors.newFixedThreadPool(POOL_SIZE)

    private val mainHandler by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Handler(Looper.getMainLooper())
    }

    override fun executeOnBackgroundThread(runnable: Runnable) {
        executorService.execute(runnable)
    }

    override fun executeOnMainThread(runnable: Runnable) {
        if (isMainThread())
            runnable.run()
        else
            mainHandler.post(runnable)
    }

    override fun isMainThread() = Looper.getMainLooper().thread.id == Thread.currentThread().id
}