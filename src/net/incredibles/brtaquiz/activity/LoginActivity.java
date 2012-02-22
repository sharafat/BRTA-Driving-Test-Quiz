package net.incredibles.brtaquiz.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.LoginController;
import net.incredibles.brtaquiz.service.TimerServiceManager;
import net.incredibles.brtaquiz.util.IndefiniteProgressingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;


/**
 * @author sharafat
 * @Created 2/16/12 2:11 PM
 */
public class LoginActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(LoginActivity.class);

    @InjectView(R.id.pin_no_input)
    private EditText pinNoInput;
    @InjectView(R.id.reg_no_input)
    private EditText regNoInput;
    @InjectView(R.id.start_test_btn)
    private Button startTestBtn;

    @Inject
    private LoginController loginController;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        prepare(savedInstanceState);
    }

    private void prepare(Bundle savedInstanceState) {
        startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                new LoginTask().execute();
            }
        });
    }


    private class LoginTask extends IndefiniteProgressingTask<Boolean> {
        @SuppressWarnings("unchecked")
        public LoginTask() {
            super(LoginActivity.this,
                    getString(R.string.authenticating),
                    new IndefiniteProgressingTask.OnTaskExecutionListener() {

                        @Override
                        public Boolean execute() {
                            return loginController.login(regNoInput.getText().toString(), pinNoInput.getText().toString());
                        }

                        @Override
                        public void onSuccess(Object result) {
                            if ((Boolean) result) {
                                new QuizPreparationTask().execute();
                            } else {
                                //TODO: Following is just a dummy. Show exam result instead
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage("User already exists.")
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).create().show();
                            }
                        }

                        @Override
                        public void onException(Exception e) {
                            logExceptionAndThrowRuntimeException("Login error", e);
                        }
                    });
        }
    }

    private class QuizPreparationTask extends IndefiniteProgressingTask<Void> {
        public QuizPreparationTask() {
            super(LoginActivity.this,
                    getString(R.string.preparing_quiz_questions),
                    new IndefiniteProgressingTask.OnTaskExecutionListener<Void>() {
                        @Override
                        public Void execute() {
                            loginController.prepareQuiz();
                            return null;
                        }

                        @Override
                        public void onSuccess(Void result) {
                            TimerServiceManager.startTimerService();
                            startActivity(new Intent(LoginActivity.this, QuestionSetListActivity.class));
                            finish();
                        }

                        @Override
                        public void onException(Exception e) {
                            logExceptionAndThrowRuntimeException("Error while preparing quiz", e);
                        }
                    });
        }
    }

    private void logExceptionAndThrowRuntimeException(String message, Exception e) {
        LOG.error(message, e);
        throw new RuntimeException(e);
    }

}
