package net.incredibles.brtaquiz.dao;

import com.google.inject.ImplementedBy;
import net.incredibles.brtaquiz.domain.Question;
import net.incredibles.brtaquiz.domain.SignSet;
import net.incredibles.brtaquiz.domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author sharafat
 * @Created 2/21/12 1:30 AM
 */
@ImplementedBy(QuestionDaoImpl.class)
public interface QuestionDao {

    void save(Question question) throws SQLException;

    Question getFirstQuestionByUserAndSignSet(User user, SignSet signSet);

    Question getById(int id);

    Question getNextQuestion(Question question);

    Question getPreviousQuestion(Question question);

    Question getQuestionBySerial(User user, SignSet signSet, long serial);

    List<Question> getUnansweredQuestions(User user);

    int getQuestionCountByQuestionSet(User user, SignSet signSet);

    Map<SignSet, Integer> getQuestionSetsWithQuestionCount(User user);

    Map<SignSet, Integer> getQuestionSetsWithMarkedCount(User user);

    Map<SignSet, List<Question>> getQuestionsGroupedBySignSet(User user);

    Map<Integer,Boolean> getQuestionsWithMarkedStatus(User user, SignSet signSet);
}
