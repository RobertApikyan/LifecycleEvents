package robertapikyan.com.lifecyclebus.observable

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

abstract class BusObserver<in T> {

    companion object {
        fun <T> create(original: BusObserver<*>, busObserver: (T) -> Unit) =
                object : BusObserver<T>() {
                    override fun onEvent(t: T) = busObserver(t)
                }.apply { id = original.id }

        fun <T> create(original: (T) -> Unit, busObserver: (T) -> Unit) =
                object : BusObserver<T>() {
                    override fun onEvent(t: T) = busObserver(t)
                }.apply { id = original.hashCode().toLong() }
    }

    var id = System.nanoTime()

    abstract fun onEvent(t: T)
}