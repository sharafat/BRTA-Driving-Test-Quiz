package net.incredibles.brtaquiz.dao;

import com.google.inject.ImplementedBy;
import net.incredibles.brtaquiz.domain.User;

import java.sql.SQLException;

/**
 * @author sharafat
 * @Created 2/16/12 3:04 PM
 */
@ImplementedBy(UserDaoImpl.class)
public interface UserDao {

    User getByRegistrationAndPinNo(String regNo, String pinNo);

    void save(User user) throws SQLException;

}
