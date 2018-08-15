package robertapikyan.com.lifecyclebus.implementation.lifecycle

/*
 * Created by Robert Apikyan on 8/15/2018.
 */

enum class PendingEventsDeliveryRules {
    IGNORE,
    IN_ORDER, // FIFO
    ONLY_LAST,
    ONLY_FIRST;
}