package net.incredibles.brtaquiz.dao;

import com.google.inject.Inject;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.testhelpers.DbTableTruncater;
import net.incredibles.brtaquiz.testhelpers.InjectedTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author sharafat
 * @Created 2/18/12 7:47 PM
 */
@RunWith(InjectedTestRunner.class)
public class UserDaoImplTest {
    private static final String SAMPLE_REG_NO = "123";
    private static final String SAMPLE_PIN_NO = "456";

    @Inject
    private UserDao userDao;

    @Test
    public void testGetByRegistrationAndPinNo_ForNewRecord() throws SQLException {
        deleteAllUsers();

        User actual = userDao.getByRegistrationAndPinNo(SAMPLE_REG_NO, SAMPLE_PIN_NO);
        assertThat(actual, nullValue());
    }

    @Test
    public void testGetByRegistrationAndPinNo_ForExistingRecord() throws SQLException {
        User expected = new User(SAMPLE_REG_NO, SAMPLE_PIN_NO);
        userDao.save(expected);
        assertThat(expected.getId(), not(0));

        User actual = userDao.getByRegistrationAndPinNo(SAMPLE_REG_NO, SAMPLE_PIN_NO);
        assertThat(actual.toString(), equalTo(expected.toString()));
    }

    private void deleteAllUsers() {
        DbTableTruncater.truncate(User.class);
    }

}
