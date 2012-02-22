package net.incredibles.brtaquiz.activity;

import android.os.Bundle;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.service.Session;
import net.incredibles.brtaquiz.service.TimerServiceManager;
import roboguice.activity.RoboActivity;

/**
 * @author sharafat
 * @Created 2/21/12 9:49 PM
 */
public class ResultActivity extends RoboActivity {
    @Inject
    Session session;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TimerServiceManager.stopTimerService();
        session.reset();
    }
}
