package net.incredibles.brtaquiz.dao;

import com.google.inject.ImplementedBy;
import net.incredibles.brtaquiz.domain.Result;
import net.incredibles.brtaquiz.domain.SignSet;

/**
 * @author sharafat
 * @Created 2/20/12 2:50 PM
 */
@ImplementedBy(ResultDaoImpl.class)
public interface ResultDao {

    Result getBySignSet(SignSet signSet);

}
