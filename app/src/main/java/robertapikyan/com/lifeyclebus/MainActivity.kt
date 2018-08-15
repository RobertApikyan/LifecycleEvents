package robertapikyan.com.lifeyclebus

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import robertapikyan.com.events.sendAsEvent
import robertapikyan.com.lifeyclebus.events.User

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onEvent(view: View?) {
        val user = User()
        user.sendAsEvent()
    }
}
