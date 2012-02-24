package net.incredibles.brtaquiz.service;

import android.app.Activity;
import android.content.Intent;
import com.google.inject.Singleton;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.activity.ResultActivity;
import net.incredibles.brtaquiz.controller.ResultController;
import net.incredibles.brtaquiz.util.IndefiniteProgressingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sharafat
 * @Created 2/25/12 12:01 AM
 */
@Singleton
public class PrepareResultTask extends IndefiniteProgressingTask<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(PrepareResultTask.class);

    public PrepareResultTask(final Activity activity, final ResultController resultController) {
        super(activity,
                activity.getString(R.string.preparing_result),
                new OnTaskExecutionListener<Void>() {
                    @Override
                    public Void execute() {
                        TimerServiceManager.stopTimerService();
                        resultController.prepareResult();
                        return null;
                    }

                    @Override
                    public void onSuccess(Void result) {
                        activity.startActivity(new Intent(activity, ResultActivity.class));
                        activity.finish();
                    }

                    @Override
                    public void onException(Exception e) {
                        LOG.error("Error while preparing result", e);
                        throw new RuntimeException(e);
                    }
                });
    }
}
