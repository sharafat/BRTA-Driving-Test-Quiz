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

    @DatabaseField(columnName = "sign_set_id", foreign = true, canBeNull = false, unique = true, index = true)
    private SignSet signSet;

    @DatabaseField(columnName = "total_questions", canBeNull = false)
    private int totalQuestions;

    @DatabaseField(canBeNull = false)
    private int answered;

    @DatabaseField(canBeNull = false)
    private int correct;

    @DatabaseField(columnName = "total_time", canBeNull = false)
    private String totalTime;

    @DatabaseField(columnName = "time_taken", canBeNull = false)
    private String timeTaken;

    public Result() {
    }

    public Result(User user, SignSet signSet, int totalQuestions, int answered, int correct, String totalTime,
                  String timeTaken) {
        this.user = user;
        this.signSet = signSet;
        this.totalQuestions = totalQuestions;
        this.answered = answered;
        this.correct = correct;
        this.totalTime = totalTime;
        this.timeTaken = timeTaken;
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

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getAnswered() {
        return answered;
    }

    public int getCorrect() {
        return correct;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setSignSet(SignSet signSet) {
        this.signSet = signSet;
    }

    public void set(User user, SignSet signSet, int totalQuestions, int answered, int correct, String totalTime,
                    String timeTaken) {
        this.user = user;
        this.signSet = signSet;
        this.totalQuestions = totalQuestions;
        this.answered = answered;
        this.correct = correct;
        this.totalTime = totalTime;
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", user=" + user +
                ", signSet=" + signSet +
                ", totalQuestions=" + totalQuestions +
                ", answered=" + answered +
                ", correct=" + correct +
                ", totalTime='" + totalTime + '\'' +
                ", timeTaken='" + timeTaken + '\'' +
                '}';
    }
}
