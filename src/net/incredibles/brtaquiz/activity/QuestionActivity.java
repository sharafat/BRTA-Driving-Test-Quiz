package net.incredibles.brtaquiz.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.QuestionController;
import net.incredibles.brtaquiz.domain.Answer;
import net.incredibles.brtaquiz.domain.Question;
import net.incredibles.brtaquiz.domain.Sign;
import net.incredibles.brtaquiz.service.TimerServiceManager;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * @author sharafat
 * @Created 2/19/12 10:36 PM
 */
public class QuestionActivity extends RoboActivity {
    @InjectView(R.id.question_set_details_text_view)
    private TextView questionSetDetailsTextView;
    @InjectView(R.id.time_remaining_text_view)
    private TextView timeRemainingTextView;
    @InjectView(R.id.question_text_view)
    private TextView questionTextView;
    @InjectView(R.id.sign_image)
    private ImageView signImageView;
    @InjectView(R.id.answers_radio_group)
    private RadioGroup answersRadioGroup;
    @InjectView(R.id.prev_btn)
    private Button prevBtn;
    @InjectView(R.id.next_btn)
    private Button nextBtn;
    @InjectResource(R.string.what_is_the_following_sign)
    private String questionText;
    @InjectResource(R.string.questions)
    private String questions;

    @Inject
    private QuestionController questionController;

    private RadioButton previouslySelectedRadioButton;
    private Handler remainingTimeUpdateHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
    }

    @Override
    protected void onStart() {
        super.onStart();
        remainingTimeUpdateHandler = new RemainingTimeUpdateHandler(this, timeRemainingTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TimerServiceManager.registerRemainingTimeUpdateHandler(remainingTimeUpdateHandler);
        prepareUI();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Dialogs.ID_REVIEW_OR_SUBMIT_CONFIRMATION_DIALOG:
                return Dialogs.createReviewOrSubmitConfirmationDialog(this, questionController);
            case Dialogs.ID_TIME_UP_DIALOG:
                return Dialogs.createTimeUpDialog(this);
            default:
                return null;
        }
    }

    private void prepareUI() {
        previouslySelectedRadioButton = null;

        Question question = questionController.getQuestion();
        displayQuestionSetDetails(question);
        displayQuestionWithSerial(question);
        displaySignImage(question);
        displayAnswers(question);

        preparePreviousButton();
        prepareNextButton();
    }

    private void displayQuestionSetDetails(Question question) {
        questionSetDetailsTextView.setText(question.getSignSet().getName()
                + " (" + questionController.getQuestionCountInCurrentQuestionSet() + " " + questions + ")");
    }

    private void displayQuestionWithSerial(Question question) {
        questionTextView.setText(question.getSerialNoInQuestionSet() + ". " + questionText);
    }

    private void displaySignImage(Question question) {
        byte[] signImage = question.getSign().getImage();
        signImageView.setImageBitmap(BitmapFactory.decodeByteArray(signImage, 0, signImage.length));
    }

    private void displayAnswers(Question question) {
        answersRadioGroup.removeAllViews();

        List<Answer> answers = question.getAnswers();
        for (Answer answer : answers) {
            Sign sign = answer.getAnswer();
            addRadioButton(sign.getId(), sign.getDescription(), sign.equals(question.getMarkedSign()));
        }
    }

    private void addRadioButton(int id, String label, boolean checked) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setChecked(checked);
        radioButton.setId(id);
        radioButton.setText(label);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton clickedRadioButton = (RadioButton) view;

                if (clickedRadioButton == previouslySelectedRadioButton) {
                    answersRadioGroup.clearCheck();
                    previouslySelectedRadioButton = null;
                    questionController.unMarkAnswer();
                } else {
                    previouslySelectedRadioButton = clickedRadioButton;
                    questionController.markAnswer(answersRadioGroup.getCheckedRadioButtonId());
                    if (!questionController.isUserReviewing() && questionController.isAllQuestionsAnswered()) {
                        showDialog(Dialogs.ID_REVIEW_OR_SUBMIT_CONFIRMATION_DIALOG);
                    }
                }
            }
        });

        answersRadioGroup.addView(radioButton);
        if (checked) {
            previouslySelectedRadioButton = radioButton;
        }
    }

    private void preparePreviousButton() {
        prevBtn.setVisibility(View.VISIBLE);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionController.previousQuestion();
                prepareUI();
            }
        });

        if (questionController.isFirstQuestion()) {
            prevBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void prepareNextButton() {
        int buttonLabelTextResourceId = R.string.next;
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionController.nextQuestion();
                prepareUI();
            }
        };

        if (questionController.isLastQuestionInCurrentSet()) {
            buttonLabelTextResourceId = R.string.choose_next_set;
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(QuestionActivity.this, QuestionSetListActivity.class));
                }
            };
        }

        nextBtn.setText(buttonLabelTextResourceId);
        nextBtn.setOnClickListener(onClickListener);
    }
}
