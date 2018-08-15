package robertapikyan.com.lifeyclebus.events

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by Robert Apikyan on 8/14/2018.
 */

class User{

    var time = System.currentTimeMillis()

    init {
        log("Events ->")
    }

    fun mm_ss():String{
        val date = Date()
        date.time = time
        val formatter = SimpleDateFormat("mm:ss")
        return formatter.format(date)
    }

    fun log(hint:String){
        Log.d(hint, "User= ${this::class.java.simpleName}," +
                " time= ${mm_ss()} thread= ${Thread.currentThread().name}")
    }
}




