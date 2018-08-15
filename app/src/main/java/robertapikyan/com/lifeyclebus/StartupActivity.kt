package robertapikyan.com.lifeyclebus

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import robertapikyan.com.events.executors.Threads
import robertapikyan.com.events.observeEvent
import robertapikyan.com.lifeyclebus.events.User

/*
 * Created by Robert Apikyan on 8/15/2018.
 */

class StartupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        observeEvent<User>(this) {
            it.log("Events <- ")
        }
    }

    fun goToMain(view: View?){
        startActivity(Intent(this,MainActivity::class.java))
    }
}