package robertapikyan.com.lifecyclebus.observable

/*
 * Created by Robert Apikyan on 1/25/2018.
 */

interface LifecycleDisposable {
    fun dispose(busObserver: BusObserver<*>)

    companion object {
        fun create(dispose:(BusObserver<*>)->Unit) = object : LifecycleDisposable {
            override fun dispose(busObserver: BusObserver<*>) = dispose(busObserver)
        }
    }
}