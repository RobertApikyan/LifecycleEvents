package robertapikyan.com.lifecyclebus.implementation

import android.arch.lifecycle.LifecycleOwner
import robertapikyan.com.lifecyclebus.executors.Threads
import robertapikyan.com.lifecyclebus.implementation.lifecycle.LifecycleDisposable
import robertapikyan.com.lifecyclebus.implementation.lifecycle.LifecycleObserver
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

class BusObservable<T : Any> {

    private val lock by lazy { ReentrantLock() }

    private val observables = HashMap<Class<T>, LinkedList<BusObserver<T>>>()

    fun observe(observer: BusObserver<T>): Disposable {
        addObserver(observer.observableClass, observer)
        return Disposable.create { removeObserver(observer.observableClass, observer) }
    }

    fun observe(lifecycleOwner: LifecycleOwner,
                observer: BusObserver<T>): Disposable {
        val lifecycleObserver = LifecycleObserver(lifecycleOwner,
                observer,
                LifecycleDisposable.create { removeObserver(observer.observableClass, it) })
        addObserver(observer.observableClass, lifecycleObserver)
        return lifecycleObserver
    }

    private fun addObserver(key: Class<T>, observer: BusObserver<T>) = lock {
        var typeObservers = observables[key]

        if (typeObservers == null) {
            typeObservers = LinkedList()
            observables[key] = typeObservers
        }

        typeObservers.add(observer)
    }

    private fun removeObserver(key: Class<T>, observer: BusObserver<T>) = lock {

        val typeObservers = observables[key]

        typeObservers?.remove(observer)
    }

    fun sendEvent(eventObj: T, sendOn: Threads) = sendOn.execute {
        lock {
            val typeObservers = observables[eventObj::class.java]

            if (typeObservers != null) {
                for (observer in typeObservers)
                    observer.receiveOn.execute {
                        observer.onEvent(eventObj)
                    }
            }
        }
    }

    private fun lock(synchronized: () -> Unit) {
        lock.lock()
        synchronized()
        lock.unlock()
    }
}