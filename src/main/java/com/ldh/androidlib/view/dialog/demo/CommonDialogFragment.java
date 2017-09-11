package com.ldh.androidlib.view.dialog.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListAdapter;


/**
 * Created by ldh on 2017/8/21.
 * 这个在屏幕旋转的时候没法保存参数
 */


public class CommonDialogFragment extends DialogFragment {
    private Context mContext;

    private AlertParams params;
    private FragmentManager mFragmentManager;

    private AlertDialog.Builder mBuilder;

    public static CommonDialogFragment createBuilder(Context context, FragmentManager manager) {
        CommonDialogFragment fragment = new CommonDialogFragment();
        //这里使用getContext（）为null,在attach方法中赋值
        fragment.init(context, manager);
        return fragment;
    }

    private CommonDialogFragment init(@NonNull Context context, FragmentManager manager) {
        mFragmentManager = manager;
        mBuilder = new AlertDialog.Builder(context);
        params = new AlertParams();
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mBuilder == null) {
            params = savedInstanceState.getParcelable("params");
            mBuilder = new AlertDialog.Builder(getContext());
            setBuilder();
        }
        AlertDialog dialog = mBuilder.create();
        return dialog;
    }

    private void setBuilder() {
        if (params.mTitle != null) {
            mBuilder.setTitle(params.mTitle);
        }
        if (params.mMessage != null) {
            mBuilder.setMessage(params.mMessage);
        }
        if (params.mPositiveButtonText != null) {
            mBuilder.setPositiveButton(params.mPositiveButtonText, null);
        }
        if (params.mNegativeButtonText != null) {
            mBuilder.setNegativeButton(params.mNegativeButtonText, null);
        }
        if (params.mNeutralButtonText != null) {
            mBuilder.setNeutralButton(params.mNeutralButtonText, null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("params", params);
        super.onSaveInstanceState(outState);
    }

    public CommonDialogFragment setCustomTitle(View customTitle) {
        mBuilder.setCustomTitle(customTitle);
        return this;
    }

    public CommonDialogFragment setTitle(String title) {
        params.mTitle = title;
        mBuilder.setTitle(title);
        return this;
    }

    public CommonDialogFragment setIcon(Drawable icon) {
        mBuilder.setIcon(icon);
        return this;
    }

    private CommonDialogFragment setIcon(int iconId) {
        params.mIconId = iconId;
        mBuilder.setIcon(iconId);
        return this;
    }

    public CommonDialogFragment setMessage(String message) {
        params.mMessage = message;
        mBuilder.setMessage(message);
        return this;
    }


    public CommonDialogFragment setItems(CharSequence[] items, OnClickListener listener) {
        mBuilder.setItems(items, listener);
        return this;
    }

    public CommonDialogFragment setCursor(final Cursor cursor, final OnClickListener listener,
                                          String labelColumn) {
        mBuilder.setCursor(cursor, listener, labelColumn);
        return this;
    }

    public CommonDialogFragment setAdapter(final ListAdapter adapter, final OnClickListener listener) {
        mBuilder.setAdapter(adapter, listener);
        return this;
    }

    public CommonDialogFragment setView(int viewLayoutResId) {
        params.mViewLayoutResId = viewLayoutResId;
        mBuilder.setView(viewLayoutResId);
        return this;
    }

    public CommonDialogFragment setView(View view) {
        mBuilder.setView(view);
        return this;
    }


    public CommonDialogFragment setNegativeButton(String buttonText, OnClickListener listener) {
        params.mNegativeButtonText = buttonText;
        mBuilder.setNegativeButton(buttonText, listener);
        return this;
    }

    public CommonDialogFragment setPositiveButton(String buttonText, OnClickListener listener) {
        params.mPositiveButtonText = buttonText;
        mBuilder.setPositiveButton(buttonText, listener);
        return this;
    }

    public CommonDialogFragment setNeutralButton(String buttonText, OnClickListener listener) {
        params.mNeutralButtonText = buttonText;
        mBuilder.setNegativeButton(buttonText, listener);
        return this;
    }

    public void show(String tag) {
        super.show(mFragmentManager, tag);
    }
}
