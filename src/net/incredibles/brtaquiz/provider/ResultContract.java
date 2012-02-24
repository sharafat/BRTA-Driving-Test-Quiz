package net.incredibles.brtaquiz.provider;

import android.net.Uri;

/**
 * User: shohan
 * Date: 2/19/12
 */
public final class ResultContract {
    public static final String AUTHORITY = "net.incredibles.brtaquiz.provider";
    private static final String SCHEME = "content://";

    public static final class Results {
        public static final String TABLE_NAME = "result";
        private static final String PATH = "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_TYPE_SINGLE_ROW = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String COL_ID = "id";
        public static final String COL_USER = "user_id";
        public static final String COL_SIGN_SET = "sign_set_id";
        public static final String COL_TOTAL_QUESTIONS = "total_questions";
        public static final String COL_ANSWERED = "answered";
        public static final String COL_CORRECT = "correct";
        public static final String COL_TOTAL_TIME = "total_time";
        public static final String COL_TIME_TAKEN = "time_taken";

        private Results() {
        }
    }

    private ResultContract() {
    }
}
