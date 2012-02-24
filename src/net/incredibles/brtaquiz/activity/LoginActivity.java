package net.incredibles.brtaquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
    public static final String KEY_RESULT_ALREADY_SAVED = "result_already_prepared";
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
                        public void onSuccess(Object existingUser) {
                            if ((Boolean) existingUser) {
                                Intent intent = new Intent(LoginActivity.this, ResultActivity.class);
                                intent.putExtra(KEY_RESULT_ALREADY_SAVED, true);
                                startActivity(intent);
                                finish();
                            } else {
                                new QuizPreparationTask().execute();
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

    /**
     * The primary purpose is to prevent systems before android.os.Build.VERSION_CODES.ECLAIR
     * from calling their default KeyEvent.KEYCODE_BACK during onKeyDown.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /** Overrides the default implementation for KeyEvent.KEYCODE_BACK so that all systems call onBackPressed(). */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /** If a Child Activity handles KeyEvent.KEYCODE_BACK. Simply override and add this method. */
    private void onBackPressed() {
        showLauncherScreen();
        finish();
    }

    private void showLauncherScreen() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
