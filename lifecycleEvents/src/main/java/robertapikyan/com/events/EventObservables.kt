@file:Suppress("MemberVisibilityCanBePrivate")

package robertapikyan.com.events

import robertapikyan.com.events.implementation.EventObservable

/**
 * Observable holder singleton
 */
@Suppress("UNCHECKED_CAST")
class EventObservables {

    // use observeEvent(observer: AbstractEventObserver<T>) and observeEvent(lifecycleOwner: LifecycleOwner,observer:
    // AbstractEventObserver<T>), do not user this observable directly
    // and do not access this field with explicit reference

    companion object {
        @PublishedApi
        internal val observable by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { EventObservable<Any>() }
    }
}