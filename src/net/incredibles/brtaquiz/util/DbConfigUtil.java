package net.incredibles.brtaquiz.util;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import net.incredibles.brtaquiz.domain.*;

/**
 * Author: sharafat
 * Date: 2/1/12 10:57 PM
 *
 *  Note: To enable all debug messages for all ORMLite classes, use the following command:
 *          adb shell setprop log.tag.ORMLite DEBUG
 */
class DbConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            SignSet.class, Sign.class, User.class, Question.class, Answer.class, Result.class, QuizTime.class
    };

    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }

}
