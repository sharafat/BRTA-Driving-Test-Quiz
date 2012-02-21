package net.incredibles.brtaquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.LoginController;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;


/**
 * @author sharafat
 * @Created 2/16/12 2:11 PM
 */
public class LoginActivity extends RoboActivity {
    @InjectView(R.id.pin_no_input)
    private EditText pinNoInput;
    @InjectView(R.id.reg_no_input)
    private EditText regNoInput;
    @InjectView(R.id.login_btn)
    private Button loginBtn;

    @Inject
    private LoginController loginController;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        prepare(savedInstanceState);
    }

    private void prepare(Bundle savedInstanceState) {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Use Loading progress dialog
                //TODO: Login always succeeds for now. Change the logic to handle login failure.
                loginController.login(regNoInput.getText().toString(), pinNoInput.getText().toString());
                loginController.prepareQuiz();
                startActivity(new Intent(LoginActivity.this, QuestionSetListActivity.class));
                finish();
            }
        });
    }

}
