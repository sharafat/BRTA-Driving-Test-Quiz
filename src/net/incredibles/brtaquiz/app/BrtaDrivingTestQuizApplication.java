package net.incredibles.brtaquiz.app;

import com.google.inject.Module;
import net.incredibles.brtaquiz.service.DbHelperManager;
import net.incredibles.brtaquiz.service.TimerServiceManager;
import roboguice.application.RoboApplication;
import roboguice.config.AbstractAndroidModule;

import java.util.List;

/**
 * @author sharafat
 * @Created 2/19/12 12:01 AM
 */
public class BrtaDrivingTestQuizApplication extends RoboApplication {
    @Override
    protected void addApplicationModules(List<Module> modules) {
        modules.add(new AbstractAndroidModule() {
            @Override
            protected void configure() {
                requestStaticInjection(DbHelperManager.class, TimerServiceManager.class);
            }
        });
    }
}
