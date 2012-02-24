package net.incredibles.brtaquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.service.QuizManager;
import net.incredibles.brtaquiz.service.TimerServiceManager;
import net.incredibles.brtaquiz.util.IndefiniteProgressingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

/**
 * @author sharafat
 * @Created 2/24/12 6:54 PM
 */
public class InstructionActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(InstructionActivity.class);

    @InjectView(R.id.instructions_text_view)
    private TextView instructionsTextView;
    @InjectView(R.id.start_test_btn)
    private Button startTestBtn;

    @InjectResource(R.string.instruction_msg)
    private String instructionMsg;
    @InjectResource(R.string.no_of_questions)
    private String noOfQuestions;
    @InjectResource(R.string.time_per_question_in_seconds)
    private String timePerQuestionInSeconds;
    @InjectResource(R.string.hours)
    private String hoursText;
    @InjectResource(R.string.minutes)
    private String minutesText;
    @InjectResource(R.string.seconds)
    private String secondsText;

    @Inject
    private QuizManager quizManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        String instructionString = String.format(instructionMsg, noOfQuestions, getTotalTime());
        LOG.trace("Formatted instruction: {}", instructionString);
        Spanned htmlString = Html.fromHtml(instructionString);
        LOG.trace("HTML Formatted instruction: {}", htmlString);
        instructionsTextView.setText(htmlString);

        startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QuizPreparationTask().execute();
            }
        });
    }

    private String getTotalTime() {
        int questions = Integer.parseInt(noOfQuestions);
        int timeInSecondsPerQues = Integer.parseInt(timePerQuestionInSeconds);
        int totalTimeInSeconds = questions * timeInSecondsPerQues;

        int hours   = (totalTimeInSeconds / (60*60)) % 24;
        int minutes = (totalTimeInSeconds / 60) % 60;
        int seconds = totalTimeInSeconds % 60;

        return (hours != 0 ? hours + " " + hoursText + " " : "")
                + (hours != 0 ? minutes + " " + minutesText + " " : minutes != 0 ? minutes + " " + minutesText + " " : "")
                + seconds + " " + secondsText;
    }


    private class QuizPreparationTask extends IndefiniteProgressingTask<Void> {
        public QuizPreparationTask() {
            super(InstructionActivity.this,
                    getString(R.string.preparing_quiz_questions),
                    new IndefiniteProgressingTask.OnTaskExecutionListener<Void>() {
                        @Override
                        public Void execute() {
                            quizManager.prepareQuiz();
                            return null;
                        }

                        @Override
                        public void onSuccess(Void result) {
                            TimerServiceManager.startTimerService();
                            startActivity(new Intent(InstructionActivity.this, QuestionSetListActivity.class));
                            finish();
                        }

                        @Override
                        public void onException(Exception e) {
                            LOG.error("Error while preparing quiz.", e);
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
