package robertapikyan.com.lifecyclebus.observable

import android.arch.lifecycle.LifecycleOwner

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

class BusObservable<T> {
    private val observables = LinkedHashMap<Long, BusObserver<T>>()

    fun observe(observer: BusObserver<T>): Disposable {
        observables[observer.id] = observer
        return Disposable.create { removeObserver(observer) }
    }

    fun observe(lifecycleOwner: LifecycleOwner, observer: BusObserver<T>) =
            observables.put(observer.id, (LifecycleBusObserver(lifecycleOwner, observer,
                    LifecycleDisposable.create { observables.remove(observer.id) })))

    private fun removeObserver(observer: BusObserver<T>) = observables.remove(observer.id)

    fun publish(t: T) = observables.entries.forEach { it.value.onEvent(t) }
}