package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author shohan
 * @Created 2/19/12
 */
public class Result {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, index = true)
    private User user;

    @DatabaseField(columnName = "sign_set_id", foreign = true, canBeNull = false)
    private SignSet signSet;

    @DatabaseField(canBeNull = false)
    private int questions;

    @DatabaseField(canBeNull = false)
    private int answered;

    @DatabaseField(canBeNull = false)
    private int correct;

    public Result() {
    }

    public Result(User user, SignSet signSet, int questions, int answered, int correct) {
        this.user = user;
        this.signSet = signSet;
        this.questions = questions;
        this.answered = answered;
        this.correct = correct;
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

    public SignSet getSignSet() {
        return signSet;
    }

    public int getQuestions() {
        return questions;
    }

    public int getAnswered() {
        return answered;
    }

    public int getCorrect() {
        return correct;
    }

    public void setSignSet(SignSet signSet) {
        this.signSet = signSet;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void set(User user, SignSet signSet, int questions, int answered, int correct) {
        this.user = user;
        this.signSet = signSet;
        this.questions = questions;
        this.answered = answered;
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", user=" + user +
                ", signSet=" + signSet +
                ", questions=" + questions +
                ", answered=" + answered +
                ", correct=" + correct +
                '}';
    }
}
