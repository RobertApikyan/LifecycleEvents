package robertapikyan.com.lifeyclebus;
/*
 * Created by Robert Apikyan on 8/15/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import robertapikyan.com.events.executors.Threads;
import robertapikyan.com.lifeyclebus.events.User;

import static robertapikyan.com.events.Events.sendEvent;

public class JavaActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User logoutEvent = new User();
        sendEvent(null, Threads.BACKGROUND);

    }
}
