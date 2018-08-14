package robertapikyan.com.lifecyclebus.observable

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

class LifecycleBusObserver<in T>(
        lifecycleOwner: LifecycleOwner,
        private val observer: BusObserver<T>,
        private val lifecycleDisposable: LifecycleDisposable
) : LifecycleObserver, BusObserver<T>() {

    private val lock by lazy { ReentrantLock() }

    private val pendingEvents: Queue<T> = LinkedList<T>()

    @Volatile
    private var isActive = true

    init {
        id = observer.id
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private fun dispatchPendings() {
        lock.lock()
        while (!pendingEvents.isEmpty()) {
            observer.onEvent(pendingEvents.poll())
        }
        lock.unlock()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lifecycleDisposable.dispose(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        isActive = true
        dispatchPendings()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        isActive = false
    }

    override fun onEvent(t: T) {
        if (isActive) {
            observer.onEvent(t)
        } else {
            lock.lock()
            pendingEvents.add(t)
            lock.unlock()
        }
    }
}