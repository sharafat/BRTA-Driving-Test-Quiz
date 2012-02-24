package net.incredibles.brtaquiz.service;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.ResultController;
import net.incredibles.brtaquiz.util.TimeUtils;
import roboguice.inject.InjectResource;

/**
 * @author shaiekh
 * @Created 2/20/12 7:01 PM
 */
public class TimerServiceManager {
    @InjectResource(R.string.time_per_question_in_seconds)
    private static String timePerQuestionInSeconds;
    @InjectResource(R.string.no_of_questions)
    private static String noOfQuestions;

    @Inject
    private static Application application;
    @Inject
    private static QuizManager quizManager;
    @Inject
    private static ResultController resultController;

    private static TimerServiceManager instance;
    private static Handler timeTickCallbackHandler;

    private Messenger serviceMessenger;
    private Messenger managerMessenger = new Messenger(new IncomingHandler());
    private long remainingTime;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            serviceMessenger = new Messenger(service);

            try {
                Message msg = Message.obtain(null, TimerService.MSG_REGISTER_CLIENT);
                msg.replyTo = managerMessenger;
                serviceMessenger.send(msg);
            } catch (RemoteException ignore) {
                // In this case the service has crashed before we could even do anything with it
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            serviceMessenger = null;
        }
    };

    public static void startTimerService() {
        if (instance == null) {
            instance = new TimerServiceManager();
            instance.doBindService();
        }
    }

    public static void stopTimerService() {
        if (instance != null) {
            instance.doUnbindService();
        }
    }

    public static void registerRemainingTimeUpdateHandler(Handler callbackHandler) {
        if (instance == null) {
            Message timeUpMsg = new Message();
            timeUpMsg.what = TimerService.MSG_TIME_UP;
            callbackHandler.sendMessage(timeUpMsg);
        } else {
            timeTickCallbackHandler = callbackHandler;
        }
    }

    private void doBindService() {
        application.bindService(new Intent(application, TimerService.class), serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    private void doUnbindService() {
        // If we have received the service, and hence registered with it, then now is the time to unregister.
        if (serviceMessenger != null) {
            try {
                Message msg = Message.obtain(null, TimerService.MSG_UNREGISTER_CLIENT);
                msg.replyTo = managerMessenger;
                serviceMessenger.send(msg);
            } catch (RemoteException ignore) {
                // There is nothing special we need to do if the service has crashed.
            }
        }

        // Detach our existing connection.
        application.unbindService(serviceConnection);

        long testDuration = TimeUtils.getTestDuration(Long.parseLong(timePerQuestionInSeconds) * 1000L,
                Integer.parseInt(noOfQuestions));
        quizManager.prepareResult(testDuration - remainingTime);

        resultController.prepareResult();

        instance = null;
    }


    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = new Bundle();
            Message relayMsg = new Message();

            switch (msg.what) {
                case TimerService.MSG_TIME_PULSE:
                    remainingTime = (Long) msg.getData().get(TimerService.KEY_TIME_PULSE);

                    bundle.putString(TimerService.KEY_TIME_PULSE, TimeUtils.getFormattedTime(remainingTime));
                    relayMsg.what = TimerService.MSG_TIME_PULSE;
                    relayMsg.setData(bundle);

                    break;
                case TimerService.MSG_TIME_UP:
                    doUnbindService();

                    relayMsg.what = TimerService.MSG_TIME_UP;

                    break;
                default:
                    super.handleMessage(msg);
            }

            try {
                if (timeTickCallbackHandler != null) {
                    timeTickCallbackHandler.sendMessage(relayMsg);
                }
            } catch (NullPointerException ignore) {
            }
        }
    }
}
