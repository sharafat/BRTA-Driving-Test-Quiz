package net.incredibles.brtaquiz.service;

import android.app.Application;
import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * @author sharafat
 * @Created 2/15/12 9:30 PM
 */
public class DbHelperManager {
    @Inject
    private static Application application;

    public static OrmLiteSqliteOpenHelper getHelper() {
        return OpenHelperManager.getHelper(application, DbHelper.class);
    }

    public static void release() {
        OpenHelperManager.releaseHelper();
    }

}
