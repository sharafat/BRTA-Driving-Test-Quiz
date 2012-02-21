package net.incredibles.brtaquiz.dao;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.service.DbHelperManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/16/12 3:10 PM
 */
@Singleton
@SuppressWarnings("unchecked")
public class UserDaoImpl implements UserDao {
    private Dao<User, Integer> dao;

    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl() throws SQLException {
        dao = DbHelperManager.getHelper().getDao(User.class);
    }

    @Override
    protected void finalize() throws Throwable {
        DbHelperManager.release();
        super.finalize();
    }

    @Override
    public User getByRegistrationAndPinNo(String regNo, String pinNo) {
        User matchingUser = new User();
        matchingUser.setRegNoAndPinNo(regNo, pinNo);

        try {
            List<User> userList = dao.queryForMatchingArgs(matchingUser);

            if (!userList.isEmpty()) {
                return userList.get(0);
            }
        } catch (SQLException e) {
            LOG.error("Query exception", e);
        }

        return null;
    }

    @Override
    public void save(User user) throws SQLException {
        dao.createOrUpdate(user);

        boolean newInstance = user.getId() == 0;
        if (newInstance) {
            user.setId(dao.extractId(user));
        }
    }

}
