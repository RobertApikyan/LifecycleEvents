package robertapikyan.com.events.implementation.lifecycle

/**
 * With this rules we can specify how to handle events when lifecycleOwner state is out of
 * (onStart - onStop) state
 */
enum class PendingEventsRules {
    IGNORE, // if lifecycleOwner state is out of onStart and onStop,
            // in that period all received events will be ignored
    IN_ORDER, // all pending events will be delivered after onStart, with the same ordering as they received (FIFO)
    REVERSE_ORDER, // all pending events will be delivered after onStop, with the same ordering as they received (LIFO)
    ONLY_LAST, // only last received event will be delivered after onStop
    ONLY_FIRST; // only first received event will be delivered after onStop
}