package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * @author sharafat
 * @Created 2/15/12 7:51 PM
 */
public class Question {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, index = true)
    private User user;

    @DatabaseField(foreign = true, canBeNull = false, index = true)
    private Sign sign;

    @DatabaseField(columnName = "sign_set_id", foreign = true, canBeNull = false, index = true)
    private SignSet signSet;

    @DatabaseField(columnName = "marked_sign_id", foreign = true)
    private Sign markedSign;

    private List<Answer> answers;
    private int serialNoInQuestionSet;

    public Question() {
    }

    public Question(User user, Sign sign, SignSet signSet) {
        this.user = user;
        this.sign = sign;
        this.signSet = signSet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Sign getSign() {
        return sign;
    }

    public SignSet getSignSet() {
        return signSet;
    }

    public void setUserAndSignAndSignSet(User user, Sign sign, SignSet signSet) {
        this.user = user;
        this.sign = sign;
        this.signSet = signSet;
    }

    public Sign getMarkedSign() {
        return markedSign;
    }

    public void setMarkedSign(Sign markedSign) {
        this.markedSign = markedSign;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public int getSerialNoInQuestionSet() {
        return serialNoInQuestionSet;
    }

    public void setSerialNoInQuestionSet(int serialNoInQuestionSet) {
        this.serialNoInQuestionSet = serialNoInQuestionSet;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", user=" + user +
                ", sign=" + sign +
                ", signSet=" + signSet +
                ", markedSign=" + markedSign +
                '}';
    }
}
