package net.incredibles.brtaquiz.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.QuestionSetListController;
import roboguice.activity.RoboListActivity;

import java.util.List;

import static net.incredibles.brtaquiz.controller.QuestionSetListController.QuestionSet;

/**
 * @author sharafat
 * @Created 2/16/12 6:09 PM
 */
public class QuestionSetListActivity extends RoboListActivity {
    static final String KEY_SELECTED_SIGN_SET = "sign_set";

    @Inject
    private QuestionSetListController questionSetListController;

    private List<QuestionSet> questionSets;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_set_list);

        //TODO: use AsyncTask for the followings
        questionSets = questionSetListController.getQuestionSets();
        setListAdapter(new QuestionSetListAdapter(this, questionSets));
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        QuestionSet selectedQuestionSet = questionSets.get(position);

        if (selectedQuestionSet.isTaken()) {
            //TODO: Show result summary
        } else {
            Intent intent = new Intent();
            intent.setClass(this, TestSummaryActivity.class);
            intent.putExtra(KEY_SELECTED_SIGN_SET, selectedQuestionSet.getSignSet());
            startActivity(intent);
        }
    }


    private class QuestionSetListAdapter extends ArrayAdapter<QuestionSet> {
        private Context context;
        private List<QuestionSet> questionSets;

        public QuestionSetListAdapter(Context context, List<QuestionSet> questionSets) {
            super(context, 0, questionSets);
            this.context = context;
            this.questionSets = questionSets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.question_set_list_entry, null);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.taken_image);
            TextView textView = (TextView) convertView.findViewById(R.id.sign_set_name);

            QuestionSet questionSet = questionSets.get(position);
            textView.setText(questionSet.getSignSet().getName());
            if (questionSet.isTaken()) {
                imageView.setImageResource(R.drawable.ic_test_taken);
            }

            return convertView;
        }
    }

}
