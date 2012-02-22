package net.incredibles.brtaquiz.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import net.incredibles.brtaquiz.service.TimerService;

/**
 * @author sharafat
 * @Created 2/22/12 11:23 PM
 */
class RemainingTimeUpdateHandler extends Handler {
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
                activity.showDialog(Dialogs.ID_TIME_UP_DIALOG);
                break;
            default:
                super.handleMessage(msg);
        }

    }
}
