package robertapikyan.com.events.implementation.lifecycle

import robertapikyan.com.events.implementation.AbstractEventObserver

interface LifecycleDisposable<T> {

    /**
     * This method will remove event observable from observable observers list,
     * with remove self strategy
     */
    fun dispose(eventObserver: AbstractEventObserver<T>)

    companion object {
        fun <T> fromLambda(dispose: (AbstractEventObserver<T>) -> Unit) = object : LifecycleDisposable<T> {
            override fun dispose(eventObserver: AbstractEventObserver<T>) = dispose(eventObserver)
        }
    }
}