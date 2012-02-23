package net.incredibles.brtaquiz.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.ResultController;
import net.incredibles.brtaquiz.domain.Result;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * User: shohan
 * Date: 2/23/12
 */
public class ResultDetailsActivity extends RoboActivity {
    @InjectView(R.id.detailed_result_list_container)
    private LinearLayout linearLayout;

    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private ResultController resultController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_result);

        List<Result> resultList = resultController.getResultList();

        for (Result result : resultList) {
            View resultDetailsEntryView = layoutInflater.inflate(R.layout.detailed_result_entry, null);

            TextView questionSetTextView = (TextView) resultDetailsEntryView.findViewById(R.id.question_set);
            TextView totalQuestionsTextView = (TextView) resultDetailsEntryView.findViewById(R.id.total_questions);
            TextView answeredTextView = (TextView) resultDetailsEntryView.findViewById(R.id.answered);
            TextView correctTextView = (TextView) resultDetailsEntryView.findViewById(R.id.correct);
            TextView incorrectTextView = (TextView) resultDetailsEntryView.findViewById(R.id.incorrect);
            TextView unansweredTextView = (TextView) resultDetailsEntryView.findViewById(R.id.unanswered);

            questionSetTextView.setText(result.getSignSet().getName());
            totalQuestionsTextView.setText(Integer.toString(result.getQuestions()));
            answeredTextView.setText(Integer.toString(result.getAnswered()));
            correctTextView.setText(Integer.toString(result.getCorrect()));
            incorrectTextView.setText(Integer.toString(result.getAnswered() - result.getCorrect()));
            unansweredTextView.setText(Integer.toString(result.getQuestions() - result.getAnswered()));

            linearLayout.addView(resultDetailsEntryView);
        }
    }

}
