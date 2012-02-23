package net.incredibles.brtaquiz.dao;

import com.google.inject.ImplementedBy;
import net.incredibles.brtaquiz.domain.QuizTime;
import net.incredibles.brtaquiz.domain.User;

import java.sql.SQLException;

/**
 * User: shohan
 * Date: 2/22/12
 */
@ImplementedBy(QuizTimeDaoImpl.class)
public interface QuizTimeDao {

    void save(QuizTime quizTime) throws SQLException;

    public QuizTime getQuizTimeByUser(User user);

}
