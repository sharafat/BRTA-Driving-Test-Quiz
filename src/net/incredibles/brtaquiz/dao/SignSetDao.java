package net.incredibles.brtaquiz.dao;

import com.google.inject.ImplementedBy;
import net.incredibles.brtaquiz.domain.SignSet;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/21/12 1:30 AM
 */
@ImplementedBy(SignSetDaoImpl.class)
public interface SignSetDao {

    void saveSignSetList(List<SignSet> signSetList) throws SQLException;

    SignSet getById(int id);

}
