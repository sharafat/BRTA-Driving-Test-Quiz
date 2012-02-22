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
    private int currentQuestionSerial;
    private int currentQuestionId;
    private int currentQuestionSetId;
    private int noOfQuestionsMarked;
    private boolean userReviewing;

    @Inject
    public Session(Application application) {
        super(application, SHARED_PREF_FILE_NAME);
    }

    public void reset() {
        clear();
        currentQuestionSerial = currentQuestionId = currentQuestionSetId = noOfQuestionsMarked = 0;
        userReviewing = false;
    }

    public User getLoggedInUser() {
        if (loggedInUser == null) {
            loggedInUser = new User();
            loggedInUser.setId(get(LOGGED_IN_USER_ID, 0));
            loggedInUser.setRegNoAndPinNo(get(LOGGED_IN_USER_REG_NO, ""), get(LOGGED_IN_USER_PIN_NO, ""));
        }

        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        set(LOGGED_IN_USER_ID, loggedInUser.getId());
        set(LOGGED_IN_USER_REG_NO, loggedInUser.getRegNo());
        set(LOGGED_IN_USER_PIN_NO, loggedInUser.getPinNo());
    }

    public int getCurrentQuestionSerial() {
        if (currentQuestionSerial == 0) {
            currentQuestionSerial = get(CURRENT_QUESTION_SERIAL, 0);
        }

        return currentQuestionSerial;
    }

    public int getCurrentQuestionId() {
        if (currentQuestionId == 0) {
            currentQuestionId = get(CURRENT_QUESTION_ID, 0);
        }

        return currentQuestionId;
    }

    public void setCurrentQuestionIdAndSerial(int questionId, int serial) {
        currentQuestionId = questionId;
        currentQuestionSerial = serial;

        set(CURRENT_QUESTION_ID, questionId);
        set(CURRENT_QUESTION_SERIAL, serial);
    }

    public int getCurrentQuestionSetId() {
        if (currentQuestionSetId == 0) {
            currentQuestionSetId = get(CURRENT_QUESTION_SET_ID, 0);
        }

        return currentQuestionSetId;
    }

    public void setCurrentQuestionSetId(int questionSetId) {
        this.currentQuestionSetId = questionSetId;
        set(CURRENT_QUESTION_SET_ID, questionSetId);
    }

    public int getNoOfQuestionsMarked() {
        if (noOfQuestionsMarked == 0) {
            noOfQuestionsMarked = get(NO_OF_QUESTIONS_MARKED, 0);
        }

        return noOfQuestionsMarked;
    }

    public void setNoOfQuestionsMarked(int noOfQuestionsMarked) {
        this.noOfQuestionsMarked = noOfQuestionsMarked;
        set(NO_OF_QUESTIONS_MARKED, noOfQuestionsMarked);
    }

    public boolean isUserReviewing() {
        if (!userReviewing) {
            userReviewing = get(USER_IS_REVIEWING, false);
        }

        return userReviewing;
    }

    public void setUserReviewing(boolean userReviewing) {
        this.userReviewing = userReviewing;
        set(USER_IS_REVIEWING, userReviewing);
    }

    interface SessionKeys {
        String LOGGED_IN_USER_ID = "user_id";
        String LOGGED_IN_USER_REG_NO = "reg_no";
        String LOGGED_IN_USER_PIN_NO = "pin_no";
        String CURRENT_QUESTION_SERIAL = "curr_ques_serial";
        String CURRENT_QUESTION_ID = "curr_ques_id";
        String CURRENT_QUESTION_SET_ID = "curr_ques_set_id";
        String NO_OF_QUESTIONS_MARKED = "no_of_ques_marked";
        String USER_IS_REVIEWING = "user_is_reviewing";
    }

}
