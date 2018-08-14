@file:Suppress("MemberVisibilityCanBePrivate")

package robertapikyan.com.lifecyclebus

import android.arch.lifecycle.LifecycleOwner
import robertapikyan.com.lifecyclebus.observable.BusObservable
import robertapikyan.com.lifecyclebus.observable.BusObserver

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

@Suppress("UNCHECKED_CAST")
object Bus {

    // use observe(observer: BusObserver<T>) and observe(lifecycleOwner: LifecycleOwner,observer:
    // BusObserver<T>), do not user this observable directly
    // and do not access this field with explicit reference
    @PublishedApi
    internal val observable = BusObservable<Any?>()

    /**
     * do not call this function with explicit reference
     */
    @PublishedApi
    internal inline fun <reified E, reified T> handleEvent(event: E, noinline observer: (T) -> Unit) {
        if (event is T) {
            observer(event)
        }
    }

    /////////////////// PUBLIC API //////////////////////////////

    fun sendEvent(event: Any?) = observable.publish(event)

    inline fun <reified T> observe(observer: BusObserver<T>) =
            observable.observe(BusObserver.create(observer)
            { it -> handleEvent<Any?, T>(it) { observer.onEvent(it) } })

    inline fun <reified T> observe(noinline observer: (T) -> Unit) =
            observable.observe(BusObserver.create(observer)
            { handleEvent(it, observer) } as BusObserver<Any?>)

    inline fun <reified T> observe(lifecycleOwner: LifecycleOwner, observer: BusObserver<T>) =
            observable.observe(lifecycleOwner, BusObserver.create(observer)
            { it -> handleEvent<Any?, T>(it) { observer.onEvent(it) } })

    inline fun <reified T> observe(lifecycleOwner: LifecycleOwner, noinline observer: (T) -> Unit) {
        observable.observe(lifecycleOwner, BusObserver.create(observer)
        { handleEvent(it, observer) } as BusObserver<Any?>)
    }
}