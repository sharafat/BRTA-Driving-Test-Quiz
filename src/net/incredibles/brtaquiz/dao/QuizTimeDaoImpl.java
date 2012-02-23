package net.incredibles.brtaquiz.dao;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import net.incredibles.brtaquiz.domain.QuizTime;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.service.DbHelperManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * User: shohan
 * Date: 2/22/12
 */
@Singleton
@SuppressWarnings("unchecked")
public class QuizTimeDaoImpl implements QuizTimeDao {

    private static final Logger LOG = LoggerFactory.getLogger(QuizTimeDaoImpl.class);

    private Dao<QuizTime, Integer> quizTimeDao;

    public QuizTimeDaoImpl() throws SQLException {
        quizTimeDao = DbHelperManager.getHelper().getDao(QuizTime.class);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelperManager.release();
    }

    @Override
    public void save(QuizTime quizTime) throws SQLException {
        quizTimeDao.createOrUpdate(quizTime);

        boolean newInstance = quizTime.getId() == 0;
        if (newInstance) {
            quizTime.setId(quizTimeDao.extractId(quizTime));
        }
    }

    @Override
    public QuizTime getQuizTimeByUser(User user) {
        QuizTime matchingResult = new QuizTime();
        matchingResult.setUser(user);

        try {
            List<QuizTime> quizTimeList = quizTimeDao.queryForMatchingArgs(matchingResult);

            if (!quizTimeList.isEmpty()) {
                return quizTimeList.get(0);
            }
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return null;
    }
}
