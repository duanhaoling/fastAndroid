package com.ldh.androidlib.view;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ldh.androidlib.R;


public class ProgressDialogFragment extends DialogFragment {

    private boolean mIsShow;
    private String mMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        LinearLayout linearLayout = new LinearLayout(activity);
        int padding = activity.getResources().getDimensionPixelOffset(R.dimen.dp_16);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setPadding(padding, padding, padding, padding);
        ProgressBar progressBar = new ProgressBar(activity);
        TextView textView = new TextView(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = activity.getResources().getDimensionPixelOffset(R.dimen.dp_24);
        textView.setText(!TextUtils.isEmpty(mMessage) ? mMessage : getString(R.string.progress_message));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        linearLayout.addView(progressBar);
        linearLayout.addView(textView, lp);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(linearLayout);

//        ProgressDialog dialog = new ProgressDialog(activity);
//        dialog.setIndeterminate(true);
//        dialog.setMessage(!TextUtils.isEmpty(mMessage) ? mMessage : getString(R.string.progress_message));
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(true);
        return dialog;
    }

    public void show(FragmentActivity activity) {
        if (!isAdded() && !mIsShow) {
            mIsShow = true;
            activity.onStateNotSaved();//allow state loss
            show(activity.getSupportFragmentManager(), "progress_dialog");
        }
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setMessage(@StringRes int id) {
        mMessage = getString(id);
    }

    @Override
    public void dismiss() {
        mIsShow = false;
        dismissAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mIsShow = false;
        super.onDismiss(dialog);
    }
}
