package net.incredibles.brtaquiz.service;

import android.app.Activity;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import net.incredibles.brtaquiz.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author sharafat
 * @Created 2/18/12 1:16 AM
 */
@RunWith(RobolectricTestRunner.class)
public class SessionTest {
    private Session session;

    @Before
    public void setup() {
        session = new Session(new Activity().getApplication());
    }

    @Test
    public void testGetSetLoggedInUser() {
        session.clear();
        User expected = new User("12345", "67890");
        session.setLoggedInUser(expected);
        setup();
        User actual = session.getLoggedInUser();
        assertThat(actual.toString(), equalTo(expected.toString()));
    }

}
