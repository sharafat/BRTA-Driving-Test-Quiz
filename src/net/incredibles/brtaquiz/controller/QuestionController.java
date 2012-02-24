package net.incredibles.brtaquiz.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.domain.Question;
import net.incredibles.brtaquiz.service.QuizManager;
import net.incredibles.brtaquiz.service.Session;

import java.util.Map;

/**
 * @author sharafat
 * @Created 2/21/12 7:48 PM
 */
@Singleton
public class QuestionController {
    @Inject
    private QuizManager quizManager;
    @Inject
    private Session session;

    public Question getQuestion() {
        return quizManager.currentQuestion();
    }

    public boolean isFirstQuestion() {
        return quizManager.isFirstQuestion(quizManager.currentQuestion());
    }

    public boolean isLastQuestionInCurrentSet() {
        return quizManager.isLastQuestionInCurrentSet(quizManager.currentQuestion());
    }

    public int getQuestionCountInCurrentQuestionSet() {
        return quizManager.getQuestionCountInCurrentQuestionSet();
    }

    public Map<Integer, Boolean> getQuestionsWithMarkedStatusInCurrentQuestionSet() {
        return quizManager.getQuestionsWithMarkedStatusInCurrentQuestionSet();
    }

    public void nextQuestion() {
        quizManager.nextQuestion();
    }

    public void previousQuestion() {
        quizManager.previousQuestion();
    }

    public void jumpToQuestion(int questionSerial) {
        quizManager.jumpToQuestion(questionSerial);
    }

    public void markAnswer(int signId, boolean updatingAnswer) {
        quizManager.markAnswer(signId, updatingAnswer);
    }

    public void unMarkAnswer() {
        quizManager.unMarkAnswer();
    }

    public boolean isAllQuestionsAnswered() {
        return quizManager.isAllQuestionsAnswered();
    }

    public void notifyUserWantsToReview() {
        session.setUserReviewing(true);
    }

    public boolean isUserReviewing() {
        return session.isUserReviewing();
    }

}
