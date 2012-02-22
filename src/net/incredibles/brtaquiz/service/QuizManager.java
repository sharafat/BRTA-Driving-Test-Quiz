package net.incredibles.brtaquiz.service;

import android.app.Application;
import android.database.Cursor;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaprovider.BrtaSignsContract;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.dao.AnswerDao;
import net.incredibles.brtaquiz.dao.QuestionDao;
import net.incredibles.brtaquiz.dao.SignDao;
import net.incredibles.brtaquiz.dao.SignSetDao;
import net.incredibles.brtaquiz.domain.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author sharafat
 * @Created 2/21/12 12:46 AM
 */
@Singleton
public class QuizManager {
    @Inject
    private Application application;
    @Inject
    private Session session;
    @Inject
    private SignSetDao signSetDao;
    @Inject
    private SignDao signDao;
    @Inject
    private QuestionDao questionDao;
    @Inject
    private AnswerDao answerDao;

    public void prepareQuiz() {
        cacheSignSetsToLocalDatabase();

        int noOfQuestions = Integer.parseInt(application.getString(R.string.no_of_questions));

        Cursor cursor = application.getContentResolver().query(
                BrtaSignsContract.Sign.CONTENT_URI, null, null, null, "RANDOM()");
        if (cursor != null && cursor.moveToFirst()) {
            int noOfQuestionSavedToDatabase = 0;
            do {
                int id = cursor.getInt(cursor.getColumnIndex(BrtaSignsContract.Sign._ID));
                String description = cursor.getString(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_DESCRIPTION));
                int signSetId = cursor.getInt(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_SIGN_SET));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_IMAGE));

                Sign sign = new Sign(id, new SignSet(signSetId), description, image);
                cacheSignToLocalDatabase(sign); //we need to cache all signs so that we can pick answers randomly from all of signs

                if (noOfQuestionSavedToDatabase++ < noOfQuestions) {
                    saveQuestionInDatabase(sign);
                }
            } while (cursor.moveToNext());
        }
    }

    public void selectQuestionSet(SignSet signSet) {
        session.setCurrentQuestionSetId(signSet.getId());
        saveFirstQuestionAsCurrentQuestionInSession();
    }

    private void saveFirstQuestionAsCurrentQuestionInSession() {
        Question firstQuestion = questionDao.getFirstQuestionByUserAndSignSet(
                session.getLoggedInUser(), new SignSet(session.getCurrentQuestionSetId()));
        session.setCurrentQuestionIdAndSerial(firstQuestion.getId(), 1);
    }

    public Question currentQuestion() {
        Question currentQuestion = getQuestionWithAnswers(session.getCurrentQuestionId());
        currentQuestion.setSerialNoInQuestionSet(session.getCurrentQuestionSerial());
        return currentQuestion;
    }

    public Question nextQuestion() {
        Question question = getQuestionWithAnswers(questionDao.getNextQuestion(currentQuestion()));
        question.setSerialNoInQuestionSet(session.getCurrentQuestionSerial() + 1);
        session.setCurrentQuestionIdAndSerial(question.getId(), question.getSerialNoInQuestionSet());
        return question;
    }

    public Question previousQuestion() {
        Question question = getQuestionWithAnswers(questionDao.getPreviousQuestion(currentQuestion()));
        question.setSerialNoInQuestionSet(session.getCurrentQuestionSerial() - 1);
        session.setCurrentQuestionIdAndSerial(question.getId(), question.getSerialNoInQuestionSet());
        return question;
    }

    public boolean isFirstQuestion(Question question) {
        return questionDao.getPreviousQuestion(question) == null;
    }

    public boolean isLastQuestion(Question question) {
        return questionDao.getNextQuestion(question) == null;
    }

    public boolean isAllQuestionsAnswered() {
        return questionDao.getUnansweredQuestions(session.getLoggedInUser()).isEmpty();
    }

    public Map<SignSet, Integer> getQuestionSetsWithQuestionCount() {
        return questionDao.getQuestionSetsWithQuestionCount(session.getLoggedInUser());
    }

    public Map<SignSet, Integer> getQuestionSetsWithMarkedCount() {
        return questionDao.getQuestionSetsWithMarkedCount(session.getLoggedInUser());
    }

    public void markAnswer(int signId) {
        saveMarkedAnswerToDatabase(new Sign(signId));
    }

    private void saveMarkedAnswerToDatabase(Sign markedSign) {
        Question currentQuestion = currentQuestion();
        currentQuestion.setMarkedSign(markedSign);
        try {
            questionDao.save(currentQuestion);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save marked answer in database. Application cannot continue.", e);
        }
    }

    public void unMarkAnswer() {
        saveMarkedAnswerToDatabase(null);
    }

    private Question getQuestionWithAnswers(int questionId) {
        return getQuestionWithAnswers(questionDao.getById(questionId));
    }

    private Question getQuestionWithAnswers(Question question) {
        if (question.getAnswers() == null) {
            question.setAnswers(getAnswers(question));
        }

        return question;
    }

    private List<Answer> getAnswers(Question question) {
        List<Answer> answers = answerDao.getByQuestion(question);

        if (answers.isEmpty()) {
            answers = generateAnswers(question);
        }

        return answers;
    }

    private List<Answer> generateAnswers(Question question) {
        List<Answer> answers = new ArrayList<Answer>();

        int alternateAnswersPerQuestion =
                Integer.parseInt(application.getString(R.string.alternate_answers_per_question));

        try {
            addAlternateAnswersToAnswerList(question, answers, alternateAnswersPerQuestion);
            addActualAnswerToAnswerList(question, answers, alternateAnswersPerQuestion);
            saveAnswersToDatabase(answers);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save question answers in database. Application cannot continue.", e);
        }

        return answers;
    }

    private void addAlternateAnswersToAnswerList(Question question, List<Answer> answers, int alternateAnswersPerQuestion) {
        List<Sign> randomSigns = signDao.getRandomSigns(question.getSign(), question.getSignSet(),
                alternateAnswersPerQuestion);

        for (Sign sign : randomSigns) {
            Answer alternateAnswer = new Answer(question, sign);
            answers.add(alternateAnswer);
        }
    }

    private void addActualAnswerToAnswerList(Question question, List<Answer> answers, int alternateAnswersPerQuestion) {
        Answer actualAnswer = new Answer(question, question.getSign());
        answers.add(new Random().nextInt(alternateAnswersPerQuestion + 1), actualAnswer);
    }

    private void saveAnswersToDatabase(List<Answer> answers) throws SQLException {
        for (Answer answer : answers) {
            answerDao.save(answer);
        }
    }

    private void cacheSignSetsToLocalDatabase() {
        try {
            signSetDao.saveSignSetList(getSignSetsFromBrtaSignsProvider());
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create SignSet in database. Application cannot continue.", e);
        }
    }

    private List<SignSet> getSignSetsFromBrtaSignsProvider() {
        List<SignSet> signSets = new ArrayList<SignSet>();

        Cursor cursor = application.getContentResolver().query(
                BrtaSignsContract.SignSet.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(BrtaSignsContract.SignSet._ID));
                String name = cursor.getString(cursor.getColumnIndex(BrtaSignsContract.SignSet.COL_NAME));
                signSets.add(new SignSet(id, name));
            } while (cursor.moveToNext());
        }

        return signSets;
    }

    private void cacheSignToLocalDatabase(Sign sign) {
        try {
            signDao.save(sign);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create Sign in database. Application cannot continue.", e);
        }
    }

    private void saveQuestionInDatabase(Sign sign) {
        User loggedInUser = session.getLoggedInUser();

        Question question = new Question(loggedInUser, sign, sign.getSignSet());
        try {
            questionDao.save(question);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create Question in database. Application cannot continue.", e);
        }
    }

}
