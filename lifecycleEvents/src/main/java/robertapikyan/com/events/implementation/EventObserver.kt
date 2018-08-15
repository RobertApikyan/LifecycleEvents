package robertapikyan.com.events.implementation

/**
 * @param T event type
 */
interface EventObserver<T> {
    fun onEvent(t: T)
}