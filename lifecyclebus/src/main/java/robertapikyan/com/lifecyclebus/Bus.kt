@file:Suppress("MemberVisibilityCanBePrivate")

package robertapikyan.com.lifecyclebus

import android.arch.lifecycle.LifecycleOwner
import robertapikyan.com.lifecyclebus.executors.Threads
import robertapikyan.com.lifecyclebus.implementation.BusObservable
import robertapikyan.com.lifecyclebus.implementation.BusObserver
import robertapikyan.com.lifecyclebus.implementation.lifecycle.PendingEventsDeliveryRules

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

@Suppress("UNCHECKED_CAST")
object Bus {

    // use observe(observer: BusObserver<T>) and observe(lifecycleOwner: LifecycleOwner,observer:
    // BusObserver<T>), do not user this observable directly
    // and do not access this field with explicit reference
    @PublishedApi
    internal val observable = BusObservable<Any>()

    /////////////////// PUBLIC API //////////////////////////////

    fun sendEvent(event: Any,
                  sendOn: Threads = Threads.MAIN) =
            observable.sendEvent(event, sendOn)

    fun <T : Any> observe(observer: BusObserver<T>) =
            observable.observe(observer as BusObserver<Any>)

    inline fun <reified T> observe(receiveOn: Threads = Threads.MAIN,
                                   pendingEventsDeliveryRules: PendingEventsDeliveryRules = PendingEventsDeliveryRules.IN_ORDER,
                                   noinline observer: (T) -> Unit) =
            observable.observe(BusObserver.fromLambda(T::class.java, receiveOn, pendingEventsDeliveryRules, observer) as BusObserver<Any>)

    fun <T> observe(lifecycleOwner: LifecycleOwner,
                    observer: BusObserver<T>) =
            observable.observe(lifecycleOwner, observer as BusObserver<Any>)

    inline fun <reified T> observe(lifecycleOwner: LifecycleOwner,
                                   receiveOn: Threads = Threads.MAIN,
                                   pendingEventsDeliveryRules: PendingEventsDeliveryRules = PendingEventsDeliveryRules.IN_ORDER,
                                   noinline observer: (T) -> Unit) =
            observable.observe(lifecycleOwner,
                    BusObserver.fromLambda(T::class.java, receiveOn, pendingEventsDeliveryRules, observer) as BusObserver<Any>)
}