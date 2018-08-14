package robertapikyan.com.lifecyclebus

import robertapikyan.com.lifecyclebus.Bus

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

fun Any?.sendEvent() = Bus.sendEvent(this)
