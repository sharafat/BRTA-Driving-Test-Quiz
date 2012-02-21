package net.incredibles.brtaquiz.dao;

import com.google.inject.ImplementedBy;
import net.incredibles.brtaquiz.domain.Sign;
import net.incredibles.brtaquiz.domain.SignSet;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/21/12 1:30 AM
 */
@ImplementedBy(SignDaoImpl.class)
public interface SignDao {

    void save(Sign sign) throws SQLException;

    List<Sign> getRandomSigns(Sign excludedSign, SignSet signSet, long limit);

}
