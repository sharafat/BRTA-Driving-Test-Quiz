package net.incredibles.brtaquiz.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.service.TimerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sharafat
 * @Created 2/22/12 11:23 PM
 */
class RemainingTimeUpdateHandler extends Handler {
    private static final Logger LOG = LoggerFactory.getLogger(RemainingTimeUpdateHandler.class);

    private Activity activity;
    private TextView textView;

    public RemainingTimeUpdateHandler(Activity activity, TextView textView) {
        this.activity = activity;
        this.textView = textView;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case TimerService.MSG_TIME_PULSE:
                textView.setText((String) msg.getData().get(TimerService.KEY_TIME_PULSE));
                break;
            case TimerService.MSG_TIME_UP:
                activity.removeDialog(Dialogs.ID_REVIEW_OR_SUBMIT_CONFIRMATION_DIALOG);
                activity.removeDialog(Dialogs.ID_FINISHING_WITH_INCOMPLETE_ANSWERS_CONFIRMATION_DIALOG);
                activity.removeDialog(Dialogs.ID_JUMP_TO_QUESTION_DIALOG);

                try {
                    activity.showDialog(Dialogs.ID_TIME_UP_DIALOG);
                } catch (Exception e) {
                    LOG.error("Time up dialog can't be displayed.", e);
                    pushQuizCompleteNotification();
                }

                break;
            default:
                super.handleMessage(msg);
        }

    }

    private void pushQuizCompleteNotification() {
        /* The following code should've started the result activity even if the application is not on focus.
*
* Intent intent = new Intent();
* intent.setClassName("net.incredibles.brtaquiz", "net.incredibles.brtaquiz.activity.ResultActivity");
* activity.startActivity(intent);
*
* However, instead of starting the activity, the following warning is found in Logcat:
* WARN/ActivityManager(68): Activity start request from 10026 stopped
*
* So, instead, I'm showing a notification message to notify the user of the end of quiz test. Clicking on it will show the result.
*/
        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Activity.NOTIFICATION_SERVICE);
        Notification quizCompleteNotification = new Notification(R.drawable.ic_launcher,
                activity.getString(R.string.quiz_finished_notification_ticker_text), System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, ResultActivity.class), 0);
        quizCompleteNotification.setLatestEventInfo(activity,
                activity.getText(R.string.quiz_finished_notification_title),
                activity.getText(R.string.quiz_finished_notification_text), pendingIntent);
        notificationManager.notify(TimerService.SERVICE_ID, quizCompleteNotification);
    }
}
