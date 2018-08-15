package robertapikyan.com.lifecyclebus.implementation.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import robertapikyan.com.lifecyclebus.implementation.BusObserver
import robertapikyan.com.lifecyclebus.implementation.Disposable
import java.util.concurrent.locks.ReentrantLock

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

internal class LifecycleObserver<T : Any>(
        lifecycleOwner: LifecycleOwner,
        private val observer: BusObserver<T>,
        private val disposable: LifecycleDisposable<T>
) : LifecycleObserver, BusObserver<T>(observer), Disposable {

    private val lock by lazy { ReentrantLock() }

    private val pendingEvents by lazy { PendingEvents<T>(observer.pendingEventsDeliveryRules) }

    @Volatile
    private var isActive = true

    init {
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

    override fun onEvent(t: T) {
        if (isActive) {
            observer.onEvent(t)
        } else {
            lock.lock()
            pendingEvents.add(t)
            lock.unlock()
        }
    }

    override fun dispose() {
        disposable.dispose(this)
    }
}