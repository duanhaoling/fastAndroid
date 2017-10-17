package com.ldh.androidlib.view.dialog.nice;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ldh on 2017/9/8.
 */
@Keep
public class NiceDialog extends BaseNiceDialog {
    private static final String KEY_LISTENER = "listener";
    private ViewConvertListener convertListener;

    public static NiceDialog init() {
        return new NiceDialog();
    }

    @Override
    public int initLayoutId() {
        return layoutId;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseNiceDialog dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }

    public NiceDialog setLayoutId(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public NiceDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            convertListener = (ViewConvertListener) savedInstanceState.getSerializable(KEY_LISTENER);
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("NiceDialog", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_LISTENER, convertListener);
    }
}
