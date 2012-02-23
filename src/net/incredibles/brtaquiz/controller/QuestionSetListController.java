package net.incredibles.brtaquiz.controller;

import android.app.Application;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.domain.SignSet;
import net.incredibles.brtaquiz.service.QuizManager;
import net.incredibles.brtaquiz.service.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sharafat
 * @Created 2/19/12 8:39 PM
 */
@Singleton
public class QuestionSetListController {
    @Inject
    Application application;
    @Inject
    private QuizManager quizManager;
    @Inject
    private Session session;

    public List<QuestionSet> getQuestionSets() {
        Map<SignSet, Integer> questionSetsWithQuestionsCount = quizManager.getQuestionSetsWithQuestionCount();
        Map<SignSet, Integer> questionSetsWithMarkedCount = quizManager.getQuestionSetsWithMarkedCount();

        List<QuestionSet> questionSetList = new ArrayList<QuestionSet>();

        for (SignSet signSet : questionSetsWithQuestionsCount.keySet()) {
            Integer questionCount = questionSetsWithQuestionsCount.get(signSet);
            if (questionCount > 0) {
                Integer markedCount = questionSetsWithMarkedCount.get(signSet);
                boolean complete = markedCount != null && markedCount.intValue() == questionCount;
                questionSetList.add(new QuestionSet(signSet, questionCount, markedCount == null ? 0 : markedCount));
            }
        }

        return questionSetList;
    }

    public void selectQuestionSet(QuestionSet questionSet) {
        quizManager.selectQuestionSet(questionSet.getSignSet());
    }

    public boolean isAllQuestionsAnswered() {
        return quizManager.isAllQuestionsAnswered();
    }

    public boolean isUserReviewing() {
        return session.isUserReviewing();
    }


    public static class QuestionSet {
        private SignSet signSet;
        private int totalQuestions;
        private int answered;

        public QuestionSet(SignSet signSet, int totalQuestions, int answered) {
            this.signSet = signSet;
            this.totalQuestions = totalQuestions;
            this.answered = answered;
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

        public boolean isComplete() {
            return answered == totalQuestions;
        }
    }
}
