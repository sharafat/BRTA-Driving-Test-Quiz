package net.incredibles.brtaquiz.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import com.cryptic.dbutil.cursorfactory.QueryLoggingEnabledCursorFactory;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import net.incredibles.brtaquiz.service.DbHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: shohan
 * Date: 2/19/12
 */
public class ResultProvider extends ContentProvider {
    private static final Logger LOG = LoggerFactory.getLogger(ResultProvider.class);
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int RESULT_TABLE = 0;
    private static final int RESULT_TABLE_SINGLE_ROW = 1;

    private OrmLiteSqliteOpenHelper db;

    static {
        matcher.addURI(ResultContract.AUTHORITY, ResultContract.Results.TABLE_NAME, RESULT_TABLE);
        matcher.addURI(ResultContract.AUTHORITY, ResultContract.Results.TABLE_NAME, RESULT_TABLE_SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        db = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = ResultContract.Results.TABLE_NAME;

        throwExceptionIfUriIsInvalid(uri, table);

        selection = getSelectionWithIdSelectionAppendedIfSignTableSingleRow(uri, selection);

        try {
            Cursor cursor = db.getReadableDatabase().queryWithFactory(new QueryLoggingEnabledCursorFactory(),
                    false, table, projection, selection, selectionArgs, null, null, sortOrder, null);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } catch (SQLiteException e) {
            LOG.error("Query Exception", e);
            return null;
        }
    }

    private void throwExceptionIfUriIsInvalid(Uri uri, String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }
    }

    private String getSelectionWithIdSelectionAppendedIfSignTableSingleRow(Uri uri, String selection) {
        if (isSignTableSingleRow(uri)) {
            selection = appendIdSelectionToSelection(uri, selection);
        }
        return selection;
    }

    private boolean isSignTableSingleRow(Uri uri) {
        return matcher.match(uri) == RESULT_TABLE_SINGLE_ROW;
    }

    private String appendIdSelectionToSelection(Uri uri, String selection) {
        String idSelection = ResultContract.Results.COL_ID + " = " + uri.getLastPathSegment();
        selection = selection == null ? idSelection : selection + " AND " + idSelection;
        return selection;
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case RESULT_TABLE:
                return ResultContract.Results.CONTENT_TYPE;
            case RESULT_TABLE_SINGLE_ROW:
                return ResultContract.Results.CONTENT_TYPE_SINGLE_ROW;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = ResultContract.Results.TABLE_NAME;

        throwExceptionIfUriIsInvalid(uri, table);

        try {
            long id = db.getWritableDatabase().insertOrThrow(table, null, values);
            notifyChangeToContentObservers(uri);
            return Uri.withAppendedPath(uri, String.valueOf(id));
        } catch (SQLiteException e) {
            LOG.error("Database Exception", e);
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQLException", e);
        }

        return null;
    }

    private void notifyChangeToContentObservers(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }
}
