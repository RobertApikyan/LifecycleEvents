package robertapikyan.com.lifecyclebus.implementation

import robertapikyan.com.lifecyclebus.executors.Threads
import robertapikyan.com.lifecyclebus.implementation.lifecycle.PendingEventsDeliveryRules

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

abstract class BusObserver<T>(val observableClass: Class<T>,
                              val receiveOn: Threads = Threads.MAIN,
                              val pendingEventsDeliveryRules: PendingEventsDeliveryRules = PendingEventsDeliveryRules.IN_ORDER) {

    companion object {
        fun <T> fromLambda(clazz: Class<T>, receiveOn: Threads, pendingEventsDeliveryRules: PendingEventsDeliveryRules, busObserver: (T) -> Unit) =
                object : BusObserver<T>(clazz, receiveOn, pendingEventsDeliveryRules) {
                    override fun onEvent(t: T) = busObserver(t)
                }
    }

    internal constructor(busObserver: BusObserver<T>):this(
            busObserver.observableClass,
            busObserver.receiveOn,
            busObserver.pendingEventsDeliveryRules
    )

    abstract fun onEvent(t: T)
}