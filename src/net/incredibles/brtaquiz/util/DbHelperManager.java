package net.incredibles.brtaquiz.util;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * @author sharafat
 * @Created 2/15/12 9:30 PM
 */
@Singleton
public class DbHelperManager {
    @Inject
    Application application;

    public SQLiteOpenHelper getSQLiteOpenHelper() {
        return OpenHelperManager.getHelper(application, DbHelper.class);
    }

    public static void release(SQLiteOpenHelper sqLiteOpenHelper) {
        if (sqLiteOpenHelper != null) {
            OpenHelperManager.releaseHelper();
            sqLiteOpenHelper = null;
        }
    }

}
