package net.incredibles.brtaquiz.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.dao.QuizTimeDao;
import net.incredibles.brtaquiz.dao.ResultDao;
import net.incredibles.brtaquiz.domain.QuizTime;
import net.incredibles.brtaquiz.domain.Result;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.service.QuizManager;
import net.incredibles.brtaquiz.service.Session;

import java.util.List;

/**
 * @author sharafat
 * @Created 2/23/12 7:28 PM
 */
@Singleton
public class ResultController {
    @Inject
    private QuizManager quizManager;
    @Inject
    private Session session;
    @Inject
    private ResultDao resultDao;
    @Inject
    private QuizTimeDao quizTimeDao;

    private List<Result> resultList;
    private int totalQuestions, answered, correct;
    private String totalTime, timeTaken;

    public void prepareResult(boolean resultAlreadySaved) {
        if (!resultAlreadySaved) {
            quizManager.prepareResult();
        }

        User loggedInUser = session.getLoggedInUser();

        totalQuestions = answered = correct = 0;

        resultList = resultDao.getByUser(loggedInUser);
        for (Result result : resultList) {
            totalQuestions += result.getQuestions();
            answered += result.getAnswered();
            correct += result.getCorrect();
        }

        QuizTime quizTime = quizTimeDao.getQuizTimeByUser(loggedInUser);
        totalTime = quizTime.getTotalTime();
        timeTaken = quizTime.getTimeTaken();
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getAnswered() {
        return answered;
    }

    public int getUnanswered() {
        return totalQuestions - answered;
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return answered - correct;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getTimeTaken() {
        return timeTaken;
    }
}
