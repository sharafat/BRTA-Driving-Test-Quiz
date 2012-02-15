package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

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

    @DatabaseField(foreign = true)
    private Sign markedSign;

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

    public void setUserAndSign(User user, Sign sign) {
        this.user = user;
        this.sign = sign;
    }

    public Sign getMarkedSign() {
        return markedSign;
    }

    public void setMarkedSign(Sign markedSign) {
        this.markedSign = markedSign;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", user=" + user +
                ", sign=" + sign +
                ", markedSign=" + markedSign +
                '}';
    }
}
