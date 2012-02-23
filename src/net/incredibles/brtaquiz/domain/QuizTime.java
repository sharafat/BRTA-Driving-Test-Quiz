package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * User: shohan
 * Date: 2/22/12
 */
public class QuizTime {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, index = true)
    private User user;

    @DatabaseField(columnName = "total_time", canBeNull = false)
    private String totalTime;

    @DatabaseField(columnName = "time_taken", canBeNull = false)
    private String timeTaken;

    public QuizTime() {
    }

    public QuizTime(User user, String totalTime, String timeTaken) {
        this.user = user;
        this.totalTime = totalTime;
        this.timeTaken = timeTaken;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    @Override
    public String toString() {
        return "QuizTime{" +
                "id=" + id +
                ", user=" + user +
                ", totalTime='" + totalTime + '\'' +
                ", timeTaken='" + timeTaken + '\'' +
                '}';
    }
}
