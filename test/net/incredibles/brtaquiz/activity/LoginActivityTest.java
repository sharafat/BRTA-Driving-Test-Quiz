package net.incredibles.brtaquiz.activity;

import android.content.Intent;
import android.widget.Button;
import com.google.inject.Inject;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.testhelpers.InjectedTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author sharafat
 * @Created 2/18/12 10:29 PM
 */
@RunWith(InjectedTestRunner.class)
public class LoginActivityTest {
    @Inject
    private LoginActivity loginActivity;

    @Before
    public void setup() {
        loginActivity.onCreate(null);
    }

    @Test
    public void pressingLoginBtnShouldStartNextActivity() {
        Button loginBtn = (Button) loginActivity.findViewById(R.id.login_btn);
        loginBtn.performClick();

        ShadowActivity shadowActivity = shadowOf(loginActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(QuestionSetList.class.getName()));
    }

}
