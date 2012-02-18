package net.incredibles.brtaquiz.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.dao.UserDao;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.service.Session;

import java.sql.SQLException;

/**
 * @author sharafat
 * @Created 2/16/12 6:02 PM
 */
@Singleton
public class LoginController {
    @Inject
    private UserDao userDao;
    @Inject
    private Session session;

    public boolean login(String regNo, String pinNo) {
        User user = retrieveUser(regNo, pinNo);
        if (user == null) {
            //TODO: Following is not a usual behavior of login anyway... Should return false right away...
            user = createUser(regNo, pinNo);
        }

        session.setLoggedInUser(user);
        return true;
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
