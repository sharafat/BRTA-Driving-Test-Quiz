package net.incredibles.brtaquiz.dao;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import net.incredibles.brtaquiz.domain.Result;
import net.incredibles.brtaquiz.domain.SignSet;
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

    public ResultDaoImpl() throws SQLException {
        resultDao = DbHelperManager.getHelper().getDao(Result.class);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelperManager.release();
    }

    @Override
    public Result getBySignSet(SignSet signSet) {
        Result matchingResult = new Result();
        matchingResult.setSignSet(signSet);

        try {
            List<Result> resultList = resultDao.queryForMatchingArgs(matchingResult);

            if (!resultList.isEmpty()) {
                return resultList.get(0);
            }
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return null;
    }
}
