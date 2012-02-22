package net.incredibles.brtaquiz.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import net.incredibles.brtaquiz.service.TimerService;

/**
 * @author sharafat
 * @Created 2/22/12 11:23 PM
 */
class RemainingTimeDisplayUpdateHandler extends Handler {
    private TextView textView;

    public RemainingTimeDisplayUpdateHandler(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void handleMessage(Message msg) {
        textView.setText((String) msg.getData().get(TimerService.KEY_TIME_PULSE));
    }
}
