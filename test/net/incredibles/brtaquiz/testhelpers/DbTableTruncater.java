package net.incredibles.brtaquiz.testhelpers;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.table.TableUtils;
import net.incredibles.brtaquiz.domain.User;
import net.incredibles.brtaquiz.service.DbHelperManager;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author sharafat
 * @Created 2/18/12 11:46 PM
 */
public class DbTableTruncater {

    @SuppressWarnings("unchecked")
    public static void truncate(Class dataClass) {
        OrmLiteSqliteOpenHelper helper = DbHelperManager.getHelper();

        try {
            TableUtils.clearTable(helper.getConnectionSource(), dataClass);

            List<User> users = helper.getDao(dataClass).queryForAll();
            assertThat(users.isEmpty(), is(true));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbHelperManager.release();
    }

}
