package net.incredibles.brtaquiz.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.domain.Question;
import net.incredibles.brtaquiz.service.QuizManager;

/**
 * @author sharafat
 * @Created 2/21/12 7:48 PM
 */
@Singleton
public class QuestionController {
    @Inject
    private QuizManager quizManager;

    public Question getQuestion() {
        return quizManager.currentQuestion();
    }

    public boolean isFirstQuestion() {
        return quizManager.isFirstQuestion(quizManager.currentQuestion());
    }

    public boolean isLastQuestion() {
        return quizManager.isLastQuestion(quizManager.currentQuestion());
    }

    public boolean isAllQuestionsAnswered() {
        return quizManager.isAllQuestionsAnswered();
    }

    public int getNoOfQuestionsInCurrentQuestionSet() {
        return quizManager.getQuestionCountInCurrentQuestionSet();
    }

    public void nextQuestion() {
        quizManager.nextQuestion();
    }

    public void previousQuestion() {
        quizManager.previousQuestion();
    }

    public void markAnswer(int signId) {
        quizManager.markAnswer(signId);
    }

    public void unMarkAnswer() {
        quizManager.unMarkAnswer();
    }

}
