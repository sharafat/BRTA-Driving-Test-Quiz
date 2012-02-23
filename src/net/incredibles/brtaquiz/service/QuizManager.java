package net.incredibles.brtaquiz.service;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaprovider.BrtaSignsContract;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.dao.*;
import net.incredibles.brtaquiz.domain.*;
import net.incredibles.brtaquiz.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboguice.inject.InjectResource;

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
    private static final Logger LOG = LoggerFactory.getLogger(QuizManager.class);

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
    @Inject
    private ResultDao resultDao;
    @Inject
    private QuizTimeDao quizTimeDao;

    @InjectResource(R.string.time_per_question_in_seconds)
    private String timePerQuestionInSeconds;
    @InjectResource(R.string.no_of_questions)
    private String noOfQuestionsFromConfig;

    private int noOfQuestions;
    private long testDuration;

    public void prepareQuiz() {
        cacheSignSetsToLocalDatabase();

        noOfQuestions = Integer.parseInt(noOfQuestionsFromConfig);
        testDuration = getTestDuration();

        Cursor cursor = application.getContentResolver().query(
                BrtaSignsContract.Sign.CONTENT_URI, null, null, null, "RANDOM()");
        if (cursor != null && cursor.moveToFirst()) {
            int noOfQuestionsSavedToDatabase = 0;
            do {
                int id = cursor.getInt(cursor.getColumnIndex(BrtaSignsContract.Sign._ID));
                String description = cursor.getString(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_DESCRIPTION));
                int signSetId = cursor.getInt(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_SIGN_SET));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(BrtaSignsContract.Sign.COL_IMAGE));

                Sign sign = new Sign(id, new SignSet(signSetId), description, image);
                cacheSignToLocalDatabase(sign); //we need to cache all signs so that we can pick answers randomly from all of signs

                if (noOfQuestionsSavedToDatabase++ < noOfQuestions) {
                    saveQuestionInDatabase(sign);
                }
            } while (cursor.moveToNext());
        }
    }

    private long getTestDuration() {
        return TimeUtils.getTestDuration(Long.parseLong(timePerQuestionInSeconds) * 1000L,
                Integer.parseInt(noOfQuestionsFromConfig));
    }

    public void prepareResult() {
        long timeTaken = TimerServiceManager.stopTimerService();
        saveResults();
        saveTimeTaken(testDuration, timeTaken);
        session.reset();
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
        Question currentQuestion = setAnswersAndSerial(getCurrentQuestion(), session.getCurrentQuestionSerial());
        LOG.debug("Current Question: {}\nAnswers: {}", currentQuestion.toString(), currentQuestion.getAnswers().toString());
        return currentQuestion;
    }

    public Question nextQuestion() {
        return setAnswersAndSerial(questionDao.getNextQuestion(getCurrentQuestion()),
                session.getCurrentQuestionSerial() + 1);
    }

    public Question previousQuestion() {
        return setAnswersAndSerial(questionDao.getPreviousQuestion(getCurrentQuestion()),
                session.getCurrentQuestionSerial() - 1);
    }

    public Question jumpToQuestion(int questionSerial) {    //questionSerial starts from 1
        Question currentQuestion = getCurrentQuestion();
        return setAnswersAndSerial(questionDao.getQuestionBySerial(
                currentQuestion.getUser(), currentQuestion.getSignSet(), questionSerial), questionSerial);
    }

    public boolean isFirstQuestion(Question question) {
        return questionDao.getPreviousQuestion(question) == null;
    }

    public boolean isLastQuestionInCurrentSet(Question question) {
        return questionDao.getNextQuestion(question) == null;
    }

    public boolean isLastQuestionInTotalExam() {
        return questionDao.getUnansweredQuestions(session.getLoggedInUser()).size() == 1;
    }

    public int getQuestionCountInCurrentQuestionSet() {
        return questionDao.getQuestionCountByQuestionSet(session.getLoggedInUser(),
                new SignSet(session.getCurrentQuestionSetId()));
    }

    public Map<SignSet, Integer> getQuestionSetsWithQuestionCount() {
        return questionDao.getQuestionSetsWithQuestionCount(session.getLoggedInUser());
    }

    public Map<SignSet, Integer> getQuestionSetsWithMarkedCount() {
        return questionDao.getQuestionSetsWithMarkedCount(session.getLoggedInUser());
    }

    public Map<Integer, Boolean> getQuestionsWithMarkedStatusInCurrentQuestionSet() {
        return questionDao.getQuestionsWithMarkedStatus(session.getLoggedInUser(),
                new SignSet(session.getCurrentQuestionSetId()));
    }

    public void markAnswer(int signId, boolean updatingAnswer) {
        saveMarkedAnswerToDatabase(new Sign(signId));

        if (!updatingAnswer) {
            session.setNoOfQuestionsMarked(session.getNoOfQuestionsMarked() + 1);
        }
    }

    public void unMarkAnswer() {
        saveMarkedAnswerToDatabase(null);
        session.setNoOfQuestionsMarked(session.getNoOfQuestionsMarked() - 1);
    }

    public boolean isAllQuestionsAnswered() {
        return session.getNoOfQuestionsMarked() == noOfQuestions;
    }

    private Question getCurrentQuestion() {
        return getQuestion(session.getCurrentQuestionId());
    }

    private Question getQuestion(int questionId) {
        return questionDao.getById(questionId);
    }

    private Question setAnswersAndSerial(Question question, int serial) {
        question = setAnswers(question);
        question.setSerialNoInQuestionSet(serial);
        session.setCurrentQuestionIdAndSerial(question.getId(), serial);
        return question;
    }

    private Question setAnswers(Question question) {
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

    private void saveQuestionInDatabase(Sign sign) {
        User loggedInUser = session.getLoggedInUser();

        Question question = new Question(loggedInUser, sign, sign.getSignSet());
        try {
            questionDao.save(question);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create Question in database. Application cannot continue.", e);
        }
    }

    private void saveAnswersToDatabase(List<Answer> answers) throws SQLException {
        for (Answer answer : answers) {
            answerDao.save(answer);
        }
    }

    private void saveMarkedAnswerToDatabase(Sign markedSign) {
        Question currentQuestion = getCurrentQuestion();
        currentQuestion.setMarkedSign(markedSign);
        LOG.debug("Saving Marked Answer. questionId: {}, Answer signId: {}", currentQuestion.getId(),
                markedSign == null ? "null" : markedSign.getId());

        try {
            questionDao.save(currentQuestion);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save marked answer in database. Application cannot continue.", e);
        }
    }

    private void cacheSignSetsToLocalDatabase() {
        try {
            signSetDao.saveSignSetList(getSignSetsFromBrtaSignsProvider());
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create SignSet in database. Application cannot continue.", e);
        }
    }

    private void cacheSignToLocalDatabase(Sign sign) {
        try {
            signDao.save(sign);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create Sign in database. Application cannot continue.", e);
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

    private void saveResults() {
        User loggedInUser = session.getLoggedInUser();

        Map<SignSet, List<Question>> questionsGroupedBySignSet =
                questionDao.getQuestionsGroupedBySignSet(loggedInUser);

        for (SignSet signSet : questionsGroupedBySignSet.keySet()) {
            List<Question> questionsInQuestionSet = questionsGroupedBySignSet.get(signSet);

            int answered = 0;
            int correct = 0;
            for (Question question : questionsInQuestionSet) {
                Sign markedSign = question.getMarkedSign();
                if (markedSign != null) {
                    answered++;

                    if (markedSign.getId() == question.getSign().getId()) {
                        correct++;
                    }
                }
            }

            Result result = new Result(loggedInUser, signSet, questionsInQuestionSet.size(), answered, correct);
            Log.d("TAG", result.toString());
            try {
                resultDao.save(result);
            } catch (SQLException e) {
                throw new RuntimeException("Cannot save result in database. Application cannot continue.", e);
            }
        }
    }

    private void saveTimeTaken(long totalTime, long timeTaken) {
        String formattedTotalTime = TimeUtils.getFormattedTime(totalTime);
        String formattedTimeTaken = TimeUtils.getFormattedTime(timeTaken);

        QuizTime quizTime = new QuizTime(session.getLoggedInUser(), formattedTotalTime, formattedTimeTaken);
        try {
            quizTimeDao.save(quizTime);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot save quiz time in database. Application cannot continue.", e);
        }
    }

}
