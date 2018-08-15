package robertapikyan.com.lifeyclebus

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import robertapikyan.com.lifecyclebus.Bus
import robertapikyan.com.lifecyclebus.implementation.lifecycle.PendingEventsDeliveryRules
import robertapikyan.com.lifeyclebus.events.Event1
import robertapikyan.com.lifeyclebus.events.Event2
import robertapikyan.com.lifeyclebus.events.Event3
import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by Robert Apikyan on 8/15/2018.
 */

class StartupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        Bus.observe<Event1>(this,pendingEventsDeliveryRules =
        PendingEventsDeliveryRules.ONLY_FIRST) {
            val date = Date()
            date.time = it.time
            val formatter = SimpleDateFormat("mm:ss")
            Log.d("Bus", "Event= ${it::class.java.simpleName}, time= ${formatter.format(date)}")
        }

        Bus.observe<Event2>(this) {
            log(it)
        }

        Bus.observe<Event3>(this) {
            log(it)
        }
    }

    fun log(any: Any) {
        Log.d("Bus", "Event= ${any::class.java.simpleName}")
    }

    fun onToMain(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}