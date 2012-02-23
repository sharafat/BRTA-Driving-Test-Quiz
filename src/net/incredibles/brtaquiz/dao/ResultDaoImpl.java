package net.incredibles.brtaquiz.dao;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import net.incredibles.brtaquiz.domain.Result;
import net.incredibles.brtaquiz.domain.SignSet;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.service.DbHelperManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/20/12 2:52 PM
 */
@Singleton
@SuppressWarnings("unchecked")
public class ResultDaoImpl implements ResultDao {
    private static final Logger LOG = LoggerFactory.getLogger(ResultDaoImpl.class);

    private Dao<Result, Integer> resultDao;
    private Dao<SignSet, Integer> signSetDao;

    public ResultDaoImpl() throws SQLException {
        resultDao = DbHelperManager.getHelper().getDao(Result.class);
        signSetDao = DbHelperManager.getHelper().getDao(SignSet.class);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelperManager.release();
    }

    @Override
    public void save(Result result) throws SQLException {
        resultDao.createOrUpdate(result);

        boolean newInstance = result.getId() == 0;
        if (newInstance) {
            result.setId(resultDao.extractId(result));
        }
    }


    @Override
    public List<Result> getByUser(User user){
        Result matchingResult = new Result();
        matchingResult.setUser(user);

        try{
            List<Result> results = resultDao.queryForMatchingArgs(matchingResult);

            for (Result result: results) {
                signSetDao.refresh(result.getSignSet());
            }

            return results;
        } catch (SQLException e){
            LOG.error("Query exception", e);
        }

        return null;
    }


}
