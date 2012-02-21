package net.incredibles.brtaquiz.service;

import android.app.Application;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.util.SharedPreferencesWrapper;

import static net.incredibles.brtaquiz.service.Session.SessionKeys.*;

/**
 * @author sharafat
 * @Created 2/16/12 4:18 PM
 */
@Singleton
public class Session extends SharedPreferencesWrapper {
    private static final String SHARED_PREF_FILE_NAME = "session";

    private User loggedInUser;

    @Inject
    public Session(Application application) {
        super(application, SHARED_PREF_FILE_NAME);
    }

    public User getLoggedInUser() {
        if (loggedInUser == null) {
            loggedInUser = new User();
            loggedInUser.setId(get(USER_ID, 0));
            loggedInUser.setRegNoAndPinNo(get(REG_NO, ""), get(PIN_NO, ""));
        }

        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        set(USER_ID, loggedInUser.getId());
        set(REG_NO, loggedInUser.getRegNo());
        set(PIN_NO, loggedInUser.getPinNo());
    }

    interface SessionKeys {
        String USER_ID = "user_id";
        String REG_NO = "reg_no";
        String PIN_NO = "pin_no";
    }

}
