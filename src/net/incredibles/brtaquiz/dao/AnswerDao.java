package net.incredibles.brtaquiz.dao;

import com.google.inject.ImplementedBy;
import net.incredibles.brtaquiz.domain.Answer;
import net.incredibles.brtaquiz.domain.Question;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/21/12 1:30 AM
 */
@ImplementedBy(AnswerDaoImpl.class)
public interface AnswerDao {

    void save(Answer answer) throws SQLException;

    List<Answer> getByQuestion(Question question);

}
