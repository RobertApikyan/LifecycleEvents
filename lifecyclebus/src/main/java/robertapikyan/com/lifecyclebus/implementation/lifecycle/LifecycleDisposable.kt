package robertapikyan.com.lifecyclebus.implementation.lifecycle

import robertapikyan.com.lifecyclebus.implementation.BusObserver

/*
 * Created by Robert Apikyan on 1/25/2018.
 */

interface LifecycleDisposable<T> {

    fun dispose(busObserver: BusObserver<T>)

    companion object {
        fun <T> create(dispose:(BusObserver<T>)->Unit) = object : LifecycleDisposable<T> {
            override fun dispose(busObserver: BusObserver<T>) = dispose(busObserver)
        }
    }
}