package robertapikyan.com.lifeyclebus

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import robertapikyan.com.lifecyclebus.Bus
import robertapikyan.com.lifecyclebus.executors.Threads
import robertapikyan.com.lifecyclebus.sendToBus
import robertapikyan.com.lifeyclebus.events.Event1
import robertapikyan.com.lifeyclebus.events.Event2
import robertapikyan.com.lifeyclebus.events.Event3

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onEvent1(view: View?) {
        val e = Event1()
        e.time = System.currentTimeMillis()
        e.sendToBus()
    }

    fun onEvent2(view: View?) {
        Event2.sendToBus()
    }

    fun onEvent3(view: View?) {
        Event3.sendToBus()
    }
}
