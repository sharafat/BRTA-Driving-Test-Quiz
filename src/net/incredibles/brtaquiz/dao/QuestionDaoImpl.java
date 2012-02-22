package net.incredibles.brtaquiz.dao;

import com.google.inject.Singleton;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import net.incredibles.brtaquiz.domain.Question;
import net.incredibles.brtaquiz.domain.Sign;
import net.incredibles.brtaquiz.domain.SignSet;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.service.DbHelperManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sharafat
 * @Created 2/21/12 1:31 AM
 */
@Singleton
public class QuestionDaoImpl implements QuestionDao {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionDaoImpl.class);

    private Dao<Question, Integer> questionDao;
    private Dao<Sign, Integer> signDao;
    private Dao<SignSet, Integer> signSetDao;
    private Dao<User, Integer> userDao;

    @SuppressWarnings("unchecked")
    public QuestionDaoImpl() throws SQLException {
        OrmLiteSqliteOpenHelper helper = DbHelperManager.getHelper();

        questionDao = helper.getDao(Question.class);
        signDao = helper.getDao(Sign.class);
        signSetDao = helper.getDao(SignSet.class);
        userDao = helper.getDao(User.class);
    }

    @Override
    protected void finalize() throws Throwable {
        DbHelperManager.release();
        super.finalize();
    }

    @Override
    public void save(Question question) throws SQLException {
        questionDao.createOrUpdate(question);

        boolean newInstance = question.getId() == 0;
        if (newInstance) {
            question.setId(questionDao.extractId(question));
        }
    }

    @Override
    public Question getFirstQuestionByUserAndSignSet(User user, SignSet signSet) {
        try {
            return questionDao.query(questionDao.queryBuilder().limit(1L)
                    .where().eq("user_id", user.getId()).and().eq("sign_set_id", signSet.getId())
                    .prepare()
            ).get(0);
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return null;
    }

    @Override
    public Question getById(int id) {
        try {
            Question question = questionDao.queryForId(id);

            userDao.refresh(question.getUser());
            signDao.refresh(question.getSign());
            signSetDao.refresh(question.getSign().getSignSet());
            signSetDao.refresh(question.getSignSet());
            signDao.refresh(question.getMarkedSign());

            return question;
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return null;
    }

    @Override
    public Question getNextQuestion(Question question) {
        try {
            List<Question> nextQuestion = questionDao.query(
                    questionDao.queryBuilder().limit(1L)
                            .where().eq("user_id", question.getUser().getId())
                            .and().eq("sign_set_id", question.getSignSet().getId())
                            .and().gt("id", question.getId())
                            .prepare()
            );

            if (!nextQuestion.isEmpty()) {
                return nextQuestion.get(0);
            }
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return null;
    }

    @Override
    public Question getPreviousQuestion(Question question) {
        try {
            List<Question> nextQuestion = questionDao.query(
                    questionDao.queryBuilder().orderBy("id", false).limit(1L)
                            .where().eq("user_id", question.getUser().getId())
                            .and().eq("sign_set_id", question.getSignSet().getId())
                            .and().lt("id", question.getId())
                            .prepare()
            );

            if (!nextQuestion.isEmpty()) {
                return nextQuestion.get(0);
            }
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return null;
    }

    @Override
    public List<Question> getUnansweredQuestions(User user) {
        try {
            return questionDao.query(questionDao.queryBuilder()
                    .where().eq("user_id", user.getId()).and().isNull("marked_sign_id").prepare());
        } catch (SQLException e) {
            LOG.error("Query exception", e);
            return null;
        }
    }

    @Override
    public Map<SignSet, Integer> getQuestionSetsWithQuestionCount(User user) {
        String query = "SELECT sign_set_id, count(id) AS _count FROM question" +
                " WHERE user_id = " + Integer.toString(user.getId()) + " GROUP BY sign_set_id";
        return getQuestionSetsWithCount(query);
    }

    @Override
    public Map<SignSet, Integer> getQuestionSetsWithMarkedCount(User user) {
        String query = "SELECT sign_set_id, count(id) AS _count FROM question" +
                " WHERE user_id = " + Integer.toString(user.getId()) + " AND marked_sign_id NOT NULL GROUP BY sign_set_id";
        return getQuestionSetsWithCount(query);
    }

    private Map<SignSet, Integer> getQuestionSetsWithCount(String query) {
        Map<SignSet, Integer> questionSetCount = new HashMap<SignSet, Integer>();

        try {
            List<String[]> result = questionDao.queryRaw(query).getResults();
            for (String[] row : result) {
                int signSetId = Integer.parseInt(row[0]);
                int count = Integer.parseInt(row[1]);

                SignSet signSet = new SignSet(signSetId);
                signSetDao.refresh(signSet);
                questionSetCount.put(signSet, count);
            }

            return questionSetCount;
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return null;
    }

}