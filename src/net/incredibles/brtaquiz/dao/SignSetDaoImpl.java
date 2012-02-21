package net.incredibles.brtaquiz.dao;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import net.incredibles.brtaquiz.domain.SignSet;
import net.incredibles.brtaquiz.service.DbHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/21/12 1:31 AM
 */
@Singleton
public class SignSetDaoImpl implements SignSetDao {
    private Dao<SignSet, Integer> dao;

    @SuppressWarnings("unchecked")
    public SignSetDaoImpl() throws SQLException {
        dao = DbHelperManager.getHelper().getDao(SignSet.class);
    }

    @Override
    protected void finalize() throws Throwable {
        DbHelperManager.release();
        super.finalize();
    }

    @Override
    public void saveSignSetList(List<SignSet> signSetList) throws SQLException {
        for (SignSet signSet : signSetList) {
            dao.createIfNotExists(signSet);
        }
    }

    @Override
    public SignSet getById(int id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }
}
