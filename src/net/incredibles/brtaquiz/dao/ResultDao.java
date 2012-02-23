package net.incredibles.brtaquiz.dao;

import com.google.inject.ImplementedBy;
import net.incredibles.brtaquiz.domain.Result;
import net.incredibles.brtaquiz.domain.User;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/20/12 2:50 PM
 */
@ImplementedBy(ResultDaoImpl.class)
public interface ResultDao {

    List<Result> getByUser(User user);

    void save(Result result) throws SQLException;

}
