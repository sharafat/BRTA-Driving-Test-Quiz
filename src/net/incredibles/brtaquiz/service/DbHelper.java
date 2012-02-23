package net.incredibles.brtaquiz.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author sharafat
 * @Created 2/15/12 7:14 PM
 *
 * Note: To enable all debug messages for all ORMLite classes, use the following command:
 *          adb shell setprop log.tag.ORMLite DEBUG
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "brta_quiz.db";
    private static final int DB_VERSION = 1;

    private static final Logger LOG = LoggerFactory.getLogger(DbHelper.class);

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            LOG.debug("Creating database tables.");

            TableUtils.createTable(connectionSource, SignSet.class);
            TableUtils.createTable(connectionSource, Sign.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Question.class);
            TableUtils.createTable(connectionSource, Answer.class);
            TableUtils.createTable(connectionSource, Result.class);
            TableUtils.createTable(connectionSource, QuizTime.class);
        } catch (SQLException e) {
            LOG.error("Error creating database.", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

}
