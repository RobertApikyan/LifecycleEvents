package robertapikyan.com.events.implementation.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import robertapikyan.com.events.implementation.AbstractEventObserver
import robertapikyan.com.events.implementation.Disposable
import java.util.concurrent.locks.ReentrantLock

internal class LifecycleObserver<T : Any>(
        lifecycleOwner: LifecycleOwner,
        private val observer: AbstractEventObserver<T>,
        private val disposable: LifecycleDisposable<T>
) : LifecycleObserver, Disposable, AbstractEventObserver<T>(observer) {

    private val lock by lazy { ReentrantLock() }

    private val pendingEvents by lazy { PendingEvents<T>(observer.rule) }

    @Volatile
    private var isActive = true

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private fun dispatchPendings() {
        lock.lock()
        while (!pendingEvents.isEmpty()) {
            onEvent(pendingEvents.poll())
        }
        lock.unlock()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        dispose()
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

    override fun event(t: T) {
        if (isActive) {
            onEvent(t)
        } else {
            lock.lock()
            pendingEvents.add(t)
            lock.unlock()
        }
    }

    override fun onEvent(t: T) = observer.event(t)

    override fun dispose() {
        disposable.dispose(this)
    }
}