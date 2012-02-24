package net.incredibles.brtaquiz.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.dao.ResultDao;
import net.incredibles.brtaquiz.dao.UserDao;
import net.incredibles.brtaquiz.domain.Result;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.service.Session;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/16/12 6:02 PM
 */
@Singleton
public class LoginController {
    @Inject
    private UserDao userDao;
    @Inject
    private ResultDao resultDao;
    @Inject
    private Session session;

    /**
     *
     * @param regNo
     * @param pinNo
     * @return true if user exists and has taken exam.
     */
    public boolean login(String regNo, String pinNo) {
        User user = retrieveUser(regNo, pinNo);

        boolean existingUser = user != null;
        if (!existingUser) {
            user = createUser(regNo, pinNo);
        }

        session.reset();
        session.setLoggedInUser(user);

        List<Result> resultList = resultDao.getByUser(user);
        return resultList != null && resultList.size() > 0;
    }

    private User retrieveUser(String regNo, String pinNo) {
        return userDao.getByRegistrationAndPinNo(regNo, pinNo);
    }

    private User createUser(String regNo, String pinNo) {
        try {
            User user = new User(regNo, pinNo);
            userDao.save(user);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create user in database. Application cannot continue.", e);
        }
    }

    UserDao getUserDao() {
        return userDao;
    }

    Session getSession() {
        return session;
    }
}
