package net.incredibles.brtaquiz.testhelpers;

import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;
import roboguice.application.RoboApplication;
import roboguice.inject.ContextScope;

public class InjectedTestRunner extends RobolectricTestRunner {

    public InjectedTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void prepareTest(Object test) {
        RoboApplication application = (RoboApplication) Robolectric.application;

        //This project's application does not extend GuiceInjectableApplication therefore we need to enter the ContextScope manually.
        Injector injector = application.getInjector();
        ContextScope scope = injector.getInstance(ContextScope.class);
        scope.enter(application);
        injector.injectMembers(test);
    }
}
