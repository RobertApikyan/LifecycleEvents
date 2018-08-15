package robertapikyan.com.lifeyclebus

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/*
 * Created by Robert Apikyan on 8/15/2018.
 */

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this);
    }
}