package net.incredibles.brtaquiz.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.QuestionController;

/**
 * @author sharafat
 * @Created 2/23/12 2:46 AM
 */
public class Dialogs {
    public static final int ID_REVIEW_OR_SUBMIT_CONFIRMATION_DIALOG = 1;
    public static final int ID_TIME_UP_DIALOG = 2;

    public static Dialog createReviewOrSubmitConfirmationDialog(final QuestionActivity questionActivity,
                                                                final QuestionController questionController) {
        return new AlertDialog.Builder(questionActivity)
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(R.string.review_or_submit_dialog_title)
                .setMessage(R.string.review_or_submit_dialog_msg)
                .setPositiveButton(R.string.review_or_submit_dialog_review_btn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                questionController.notifyUserWantsToReview();
                                questionActivity.removeDialog(ID_REVIEW_OR_SUBMIT_CONFIRMATION_DIALOG);
                                questionActivity.onResume();
                            }
                        })
                .setNegativeButton(R.string.review_or_submit_dialog_finish_btn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(questionActivity, ResultActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                questionActivity.startActivity(intent);
                                questionActivity.removeDialog(ID_REVIEW_OR_SUBMIT_CONFIRMATION_DIALOG);
                                questionActivity.finish();
                            }
                        })
                .create();
    }

    public static Dialog createTimeUpDialog(final Activity activity) {
        return new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(R.string.timeup_dialog_title)
                .setMessage(R.string.timeup_dialog_msg)
                .setPositiveButton(R.string.timeup_dialog_ok_btn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.startActivity(new Intent(activity, ResultActivity.class));
                                activity.removeDialog(ID_TIME_UP_DIALOG);
                                activity.finish();
                            }
                        })
                .create();
    }

}
