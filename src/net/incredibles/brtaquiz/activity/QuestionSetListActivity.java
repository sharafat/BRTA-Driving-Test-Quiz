package net.incredibles.brtaquiz.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.QuestionSetListController;
import net.incredibles.brtaquiz.controller.ResultController;
import net.incredibles.brtaquiz.service.PrepareResultTask;
import net.incredibles.brtaquiz.service.TimerServiceManager;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.List;

import static net.incredibles.brtaquiz.controller.QuestionSetListController.QuestionSet;

/**
 * @author sharafat
 * @Created 2/16/12 6:09 PM
 */
public class QuestionSetListActivity extends RoboListActivity {
    @InjectView(R.id.time_remaining_text_view)
    private TextView timeRemainingTextView;
    @InjectResource(R.string.questions)
    private String questions;
    @InjectResource(R.string.answered)
    private String answered;
    @Inject
    private LayoutInflater layoutInflater;

    @Inject
    private QuestionSetListController questionSetListController;
    @Inject
    private ResultController resultController;


    private List<QuestionSet> questionSets;
    private Handler remainingTimeUpdateHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_set_list);

        remainingTimeUpdateHandler = new RemainingTimeUpdateHandler(this, timeRemainingTextView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TimerServiceManager.registerRemainingTimeUpdateHandler(remainingTimeUpdateHandler);
        questionSets = questionSetListController.getQuestionSets();
        setListAdapter(new QuestionSetListAdapter(questionSets));
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        questionSetListController.selectQuestionSet(questionSets.get(position));
        startActivity(new Intent(this, QuestionActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_set_list_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_finish_test:
                if (questionSetListController.isAllQuestionsAnswered()) {
                    new PrepareResultTask(this, resultController).execute();
                } else {
                    showDialog(Dialogs.ID_FINISHING_WITH_INCOMPLETE_ANSWERS_CONFIRMATION_DIALOG);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Dialogs.ID_TIME_UP_DIALOG:
                return Dialogs.createTimeUpDialog(this, resultController);
            case Dialogs.ID_FINISHING_WITH_INCOMPLETE_ANSWERS_CONFIRMATION_DIALOG:
                return Dialogs.createFinishingWithIncompleteAnswersConfirmationDialog(this, resultController);
            default:
                return null;
        }
    }


    private class QuestionSetListAdapter extends ArrayAdapter<QuestionSet> {
        private List<QuestionSet> questionSets;

        public QuestionSetListAdapter(List<QuestionSet> questionSets) {
            super(QuestionSetListActivity.this, 0, questionSets);
            this.questionSets = questionSets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.question_set_list_entry, null);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.complete_icon);
            TextView questionSetNameTextView = (TextView) convertView.findViewById(R.id.question_set_details_text_view);
            TextView questionSetDetailsTextView = (TextView) convertView.findViewById(R.id.question_set_details);

            QuestionSet questionSet = questionSets.get(position);
            questionSetNameTextView.setText(questionSet.getSignSet().getName());
            questionSetDetailsTextView.setText(getQuestionSetDetailsText(questionSet));
            if (questionSet.isComplete()) {
                imageView.setImageResource(R.drawable.ic_test_complete);
            }

            return convertView;
        }

        private String getQuestionSetDetailsText(QuestionSet questionSet) {
            return questions + ": " + questionSet.getTotalQuestions() + "   "
                    + answered + ": " + questionSet.getAnswered();
        }
    }

}
