package net.incredibles.brtaquiz.dao;

import com.google.inject.Singleton;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import net.incredibles.brtaquiz.domain.Answer;
import net.incredibles.brtaquiz.domain.Question;
import net.incredibles.brtaquiz.domain.Sign;
import net.incredibles.brtaquiz.service.DbHelperManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/21/12 3:20 PM
 */
@Singleton
public class AnswerDaoImpl implements AnswerDao {
    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

    private Dao<Answer, Integer> answerDao;
    private Dao<Sign, Integer> signDao;

    @SuppressWarnings("unchecked")
    public AnswerDaoImpl() throws SQLException {
        OrmLiteSqliteOpenHelper helper = DbHelperManager.getHelper();

        answerDao = helper.getDao(Answer.class);
        signDao = helper.getDao(Sign.class);
    }

    @Override
    protected void finalize() throws Throwable {
        DbHelperManager.release();
        super.finalize();
    }

    @Override
    public void save(Answer answer) throws SQLException {
        answerDao.createOrUpdate(answer);
    }

    @Override
    public List<Answer> getByQuestion(Question question) {
        List<Answer> answers = new ArrayList<Answer>();

        Answer example = new Answer(question, null);
        try {
            answers = answerDao.queryForMatchingArgs(example);
            for (Answer answer : answers) {
                signDao.refresh(answer.getAnswer());
            }
            return answers;
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return answers;
    }
}
