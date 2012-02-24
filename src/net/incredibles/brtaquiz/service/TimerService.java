package net.incredibles.brtaquiz.service;

/**
 * @author shaiekh
 * @Created 2/19/12 12:22 PM
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.activity.QuestionSetListActivity;
import net.incredibles.brtaquiz.util.ForeGroundServiceCompat;
import net.incredibles.brtaquiz.util.TimeUtils;
import roboguice.inject.InjectResource;
import roboguice.service.RoboService;

@Singleton
public class TimerService extends RoboService {
    public static final int SERVICE_ID = 8271;
    private static final long TICK_INTERVAL_IN_MILLIS = 1000;

    public static final String KEY_TIME_PULSE = "KEY_TIME_PULSE";
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_TIME_PULSE = 3;
    public static final int MSG_TIME_UP = 4;

    @InjectResource(R.string.quiz_running_notification_ticker_text)
    private String notificationTickerText;
    @InjectResource(R.string.quiz_running_notification_title)
    private String notificationTitle;
    @InjectResource(R.string.quiz_running_notification_text)
    private String notificationText;
    @InjectResource(R.string.time_per_question_in_seconds)
    private String timePerQuestionInSeconds;
    @InjectResource(R.string.no_of_questions)
    private String noOfQuestions;

    @Inject
    private NotificationManager notificationManager;

    private Messenger clientMessenger;
    private Messenger serviceMessenger;
    private Notification notification;
    private PendingIntent pendingIntentForNotification;
    private ForeGroundServiceCompat foreGroundServiceCompat;
    private CountDownTimer countDownTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        serviceMessenger = new Messenger(new IncomingHandler());
        notification = new Notification(R.drawable.ic_launcher, notificationTickerText, System.currentTimeMillis());
        pendingIntentForNotification = PendingIntent.getActivity(this, 0,
                new Intent(this, QuestionSetListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
        foreGroundServiceCompat = new ForeGroundServiceCompat(this);

        long testDuration = getTestDuration();
        countDownTimer = new RemainingTimeCount(testDuration, TICK_INTERVAL_IN_MILLIS);
        setLatestEventInfoNotification(TimeUtils.getFormattedTime(testDuration));
        countDownTimer.start();

        foreGroundServiceCompat.startForeground(SERVICE_ID, notification);
    }

    private long getTestDuration() {
        return TimeUtils.getTestDuration(Long.parseLong(timePerQuestionInSeconds) * 1000L, Integer.parseInt(noOfQuestions));
    }

    private void setLatestEventInfoNotification(String remainingTime) {
        notification.setLatestEventInfo(this, notificationTitle, notificationText + "   " + remainingTime,
                pendingIntentForNotification);
    }

    private void sendMessageToUI(long intValueToSend) {
        try {
            if (clientMessenger != null) {
                Bundle bundle = new Bundle();
                bundle.putLong(KEY_TIME_PULSE, intValueToSend);
                Message msg = Message.obtain();
                msg.what = MSG_TIME_PULSE;
                msg.setData(bundle);
                clientMessenger.send(msg);
            }
        } catch (RemoteException e) {
            clientMessenger = null;
        }
    }

    private class RemainingTimeCount extends CountDownTimer {

        public RemainingTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            try {
                clientMessenger.send(Message.obtain(null, MSG_TIME_UP));
            } catch (RemoteException ignore) {
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendMessageToUI(millisUntilFinished);
            setLatestEventInfoNotification(TimeUtils.getFormattedTime(millisUntilFinished));
            notificationManager.notify(SERVICE_ID, notification);
        }
    }


    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    clientMessenger = msg.replyTo;
                    break;
                case MSG_UNREGISTER_CLIENT:
                    clientMessenger = null;
                    countDownTimer.cancel();
                    foreGroundServiceCompat.stopForeground(SERVICE_ID);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

}
