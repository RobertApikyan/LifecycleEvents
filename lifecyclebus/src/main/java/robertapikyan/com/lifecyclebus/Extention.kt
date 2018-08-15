package robertapikyan.com.lifecyclebus

import robertapikyan.com.lifecyclebus.executors.Threads

/*
 * Created by Robert Apikyan on 1/24/2018.
 */

fun Any.sendToBus(sendOn: Threads = Threads.MAIN,
                  receiveOn: Threads = Threads.MAIN) = Bus.sendEvent(this, sendOn)
