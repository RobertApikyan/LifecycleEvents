package robertapikyan.com.events.implementation.lifecycle

import java.util.*

class PendingEvents<T>(private val policy: PendingEventsRules,
                       private val events: LinkedList<T> = LinkedList()) :
        Queue<T> by events {

    override fun add(element: T): Boolean {
        return when (policy) {

            PendingEventsRules.IN_ORDER -> events.add(element)

            PendingEventsRules.REVERSE_ORDER -> {
                events.addFirst(element)
                return true
            }

            PendingEventsRules.ONLY_FIRST -> {
                if (events.isEmpty()) {
                    events.addFirst(element)
                    return true
                }
                return false
            }

            PendingEventsRules.ONLY_LAST -> {
                events.clear()
                events.addFirst(element)
                return true
            }

            PendingEventsRules.IGNORE -> false

            PendingEventsRules.IMMEDIATE -> false
        }
    }
}