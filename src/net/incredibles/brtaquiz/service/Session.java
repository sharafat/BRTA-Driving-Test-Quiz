package net.incredibles.brtaquiz.service;

import android.app.Application;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.util.SharedPreferencesWrapper;

/**
 * @author sharafat
 * @Created 2/16/12 4:18 PM
 */
@Singleton
public class Session extends SharedPreferencesWrapper {
    private static final String SHARED_PREF_FILE_NAME = "session";

    private Application application;
    private User loggedInUser;

    @Inject
    public Session(Application application) {
        super(application, SHARED_PREF_FILE_NAME);
        this.application = application;
    }

    public User getLoggedInUser() {
        if (loggedInUser == null) {
            loggedInUser = new User();
            loggedInUser.setId(get(application.getString(R.string.key_user_id), 0));
            loggedInUser.setRegNoAndPinNo(get(application.getString(R.string.key_reg_no), ""),
                    get(application.getString(R.string.key_pin_no), ""));
        }

        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        set(application.getString(R.string.key_user_id), loggedInUser.getId());
        set(application.getString(R.string.key_reg_no), loggedInUser.getRegNo());
        set(application.getString(R.string.key_pin_no), loggedInUser.getPinNo());
    }
}
