package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author sharafat
 * @Created 2/15/12 8:37 PM
 */
public class Answer {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, index = true)
    private Question question;

    @DatabaseField(columnName = "answer_sign_id", foreign = true, canBeNull = false)
    private Sign answer;

    public Answer() {
    }

    public Answer(Question question, Sign answer) {
        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public Sign getAnswer() {
        return answer;
    }

    public void setQuestionAndAnswer(Question question, Sign answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", question=" + question +
                ", answer=" + answer +
                '}';
    }
}
