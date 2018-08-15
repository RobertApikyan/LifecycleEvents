package robertapikyan.com.events.implementation

import robertapikyan.com.events.executors.Threads
import robertapikyan.com.events.implementation.lifecycle.PendingEventsRules

abstract class AbstractEventObserver<T>(val observableClass: Class<T>,
                                        val receiveOn: Threads = Threads.MAIN,
                                        val rule: PendingEventsRules = PendingEventsRules.IN_ORDER)
    : EventObserver<T> {

    companion object {
        fun <T> fromLambda(clazz: Class<T>, receiveOn: Threads, rule: PendingEventsRules, eventObserver: (T) -> Unit) =
                object : AbstractEventObserver<T>(clazz, receiveOn, rule) {
                    override fun onEvent(t: T) = eventObserver(t)
                }
    }

    internal constructor(eventObserver: AbstractEventObserver<T>) : this(
            eventObserver.observableClass,
            eventObserver.receiveOn,
            eventObserver.rule
    )

    internal open fun event(t: T) = receiveOn.execute { onEvent(t) }
}