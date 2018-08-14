package robertapikyan.com.lifecyclebus.observable

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

interface Disposable {
    fun dispose()

    companion object {
        fun create(dispose: () -> Unit) = object : Disposable {
            override fun dispose() = dispose()
        }
    }
}