package net.incredibles.brtaquiz.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import net.incredibles.brtaquiz.R;
import net.incredibles.brtaquiz.controller.QuestionController;
import net.incredibles.brtaquiz.controller.ResultController;
import net.incredibles.brtaquiz.service.PrepareResultTask;

/**
 * @author sharafat
 * @Created 2/23/12 2:46 AM
 */
public class Dialogs {
    public static final int ID_REVIEW_OR_SUBMIT_CONFIRMATION_DIALOG = 1;
    public static final int ID_TIME_UP_DIALOG = 2;
    public static final int ID_FINISHING_WITH_INCOMPLETE_ANSWERS_CONFIRMATION_DIALOG = 3;
    public static final int ID_JUMP_TO_QUESTION_DIALOG = 4;

    public static Dialog createReviewOrSubmitConfirmationDialog(final QuestionActivity questionActivity,
                                                                final QuestionController questionController,
                                                                final ResultController resultController) {
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
                                questionActivity.removeDialog(ID_REVIEW_OR_SUBMIT_CONFIRMATION_DIALOG);
                                new PrepareResultTask(questionActivity, resultController).execute();
                            }
                        })
                .create();
    }

    public static Dialog createTimeUpDialog(final Activity activity, final ResultController resultController) {
        return new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(R.string.timeup_dialog_title)
                .setMessage(R.string.timeup_dialog_msg)
                .setPositiveButton(R.string.timeup_dialog_ok_btn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.removeDialog(ID_TIME_UP_DIALOG);
                                new PrepareResultTask(activity, resultController).execute();
                            }
                        })
                .create();
    }

    public static Dialog createFinishingWithIncompleteAnswersConfirmationDialog(final Activity activity,
                                                                                final ResultController resultController) {
        return new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(R.string.finish_test_with_incomplete_answers_dialog_title)
                .setMessage(R.string.finish_test_with_incomplete_answers_dialog_msg)
                .setPositiveButton(R.string.finish_test_with_incomplete_answers_resume_btn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.removeDialog(ID_FINISHING_WITH_INCOMPLETE_ANSWERS_CONFIRMATION_DIALOG);
                            }
                        })
                .setNegativeButton(R.string.review_or_submit_dialog_finish_btn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.removeDialog(ID_FINISHING_WITH_INCOMPLETE_ANSWERS_CONFIRMATION_DIALOG);
                                new PrepareResultTask(activity, resultController).execute();
                            }
                        })
                .create();
    }

}
