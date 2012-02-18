package net.incredibles.brtaquiz.util;

import android.app.Application;
import android.content.SharedPreferences;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author sharafat
 * @Created 2/18/12 12:01 AM
 */
@RunWith(RobolectricTestRunner.class)
public class SharedPreferencesWrapperTest {
    private static final String KEY = "key";
    private SharedPreferencesWrapper sharedPreferencesWrapper;

    @Before
    public void setup() {
        sharedPreferencesWrapper = new SharedPreferencesWrapper(new Application(), "test");
    }

    @Test
    public void testSet_Boolean() {
        boolean expectedResult = true;

        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, expectedResult);
        assertThat(isSuccessfullyCommitted, is(true));
        assertThat(sharedPreferencesWrapper.get(KEY, false), equalTo(expectedResult));
    }

    @Test
    public void testSet_String() {
        String expectedResult = "test";

        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, expectedResult);
        assertThat(isSuccessfullyCommitted, is(true));
        assertThat(sharedPreferencesWrapper.get(KEY, ""), equalTo(expectedResult));
    }

    @Test
    public void testSet_Int() {
        int expectedResult = Integer.MAX_VALUE;

        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, expectedResult);
        assertThat(isSuccessfullyCommitted, is(true));
        assertThat(sharedPreferencesWrapper.get(KEY, 0), equalTo(expectedResult));
    }

    @Test
    public void testSet_Long() {
        Long expectedResult = Long.MAX_VALUE;

        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, expectedResult);
        assertThat(isSuccessfullyCommitted, is(true));
        assertThat(sharedPreferencesWrapper.get(KEY, 0L), equalTo(expectedResult));
    }

    @Test
    public void testSet_Float() {
        float expectedResult = Float.MAX_VALUE;

        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, expectedResult);
        assertThat(isSuccessfullyCommitted, is(true));
        assertThat(sharedPreferencesWrapper.get(KEY, 0.0F), equalTo(expectedResult));
    }

    @Test
    public void testRemove() {
        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, KEY);
        assertThat(isSuccessfullyCommitted, is(true));

        isSuccessfullyCommitted = sharedPreferencesWrapper.remove(KEY);
        assertThat(isSuccessfullyCommitted, is(true));
        assertThat(sharedPreferencesWrapper.contains(KEY), is(false));
    }

    @Test
    public void testClear() {
        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, KEY);
        assertThat(isSuccessfullyCommitted, is(true));

        isSuccessfullyCommitted = sharedPreferencesWrapper.clear();
        assertThat(isSuccessfullyCommitted, is(true));
        assertThat(sharedPreferencesWrapper.getAll().isEmpty(), is(true));
    }

    @Test
    public void testRegisterChangeListener() {
        sharedPreferencesWrapper.registerChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                assertThat(sharedPreferences.getString(key, ""), equalTo(KEY));
            }
        });

        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, KEY);
        assertThat(isSuccessfullyCommitted, is(true));
    }

    @Test
    public void testUnregisterChangeListener() {
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                fail("SharedPreferencesChangeListener should have been unregistered.");
            }
        };

        sharedPreferencesWrapper.registerChangeListener(listener);
        sharedPreferencesWrapper.unregisterChangeListener(listener);

        boolean isSuccessfullyCommitted = sharedPreferencesWrapper.set(KEY, KEY);
        assertThat(isSuccessfullyCommitted, is(true));
    }

}
