package robertapikyan.com.events.implementation

interface Disposable {

    /**
     * This method will remove event observable from observable observers list
     */
    fun dispose()

    companion object {
        fun fromLambda(dispose: () -> Unit) = object : Disposable {
            override fun dispose() = dispose()
        }
    }
}