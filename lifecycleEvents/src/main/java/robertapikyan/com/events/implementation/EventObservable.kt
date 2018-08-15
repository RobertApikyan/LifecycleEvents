package robertapikyan.com.events.implementation

import android.arch.lifecycle.LifecycleOwner
import robertapikyan.com.events.executors.Threads
import robertapikyan.com.events.implementation.lifecycle.LifecycleDisposable
import robertapikyan.com.events.implementation.lifecycle.LifecycleObserver
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * @param T event type
 */
class EventObservable<T : Any> {

    private val lock by lazy { ReentrantLock() }

    private val observables = HashMap<Class<T>, LinkedList<AbstractEventObserver<T>>>()

    fun observe(observer: AbstractEventObserver<T>): Disposable {
        addObserver(observer.observableClass, observer)
        return Disposable.fromLambda { removeObserver(observer.observableClass, observer) }
    }

    fun observe(lifecycleOwner: LifecycleOwner,
                observer: AbstractEventObserver<T>): Disposable {
        val lifecycleObserver = LifecycleObserver(lifecycleOwner,
                observer,
                LifecycleDisposable.fromLambda { removeObserver(observer.observableClass, it) })
        addObserver(observer.observableClass, lifecycleObserver)
        return lifecycleObserver
    }

    fun sendEvent(eventObj: T, sendOn: Threads) = sendOn.execute {
        lockObservables {
            val typeObservers = observables[eventObj::class.java]

            if (typeObservers != null) {
                for (observer in typeObservers)
                    observer.event(eventObj)
            }
        }
    }

    private fun addObserver(key: Class<T>, observer: AbstractEventObserver<T>) = lockObservables {
        var typeObservers = observables[key]

        if (typeObservers == null) {
            typeObservers = LinkedList()
            observables[key] = typeObservers
        }

        typeObservers.add(observer)
    }

    private fun removeObserver(key: Class<T>, observer: AbstractEventObserver<T>) = lockObservables {

        val typeObservers = observables[key] ?: return@lockObservables

        typeObservers.remove(observer)

        if (typeObservers.isEmpty()) {
            observables.remove(key)
        }
    }

    private fun lockObservables(synchronized: () -> Unit) {
        lock.lock()
        synchronized()
        lock.unlock()
    }
}