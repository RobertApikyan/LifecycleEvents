package robertapikyan.com.lifecyclebus.implementation.lifecycle

import java.util.*

/*
 * Created by Robert Apikyan on 8/15/2018.
 */

class PendingEvents<T>(private val policy: PendingEventsDeliveryRules,
                       private val events: LinkedList<T> = LinkedList()) :
        Queue<T> by events {

    override fun add(element: T): Boolean {
        return when (policy) {

            PendingEventsDeliveryRules.IN_ORDER -> events.add(element)

            PendingEventsDeliveryRules.ONLY_FIRST -> {
                if (events.isEmpty()) {
                    events.addFirst(element)
                    return true
                }
                return false
            }

            PendingEventsDeliveryRules.ONLY_LAST -> {
                events.clear()
                events.addFirst(element)
                return true
            }

            PendingEventsDeliveryRules.IGNORE -> false

        }
    }
}