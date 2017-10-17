package com.ldh.androidlib.view.dialog.nice;

import android.support.annotation.Keep;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ldh on 2017/9/8.
 */
@Keep
public class DialogViewHolder {
    private SparseArray<View> views;
    private View convertView;
    private BaseNiceDialog dialog;

    private DialogViewHolder(View view, BaseNiceDialog dialog) {
        convertView = view;
        this.dialog = dialog;
        views = new SparseArray<>();
    }

    public static DialogViewHolder create(View view, BaseNiceDialog dialog) {
        return new DialogViewHolder(view, dialog);
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.append(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }

    /**
     * 1，点击如果跳转页面需要dismiss
     *
     * 2，如果点击事件有网络请求回调并更新View，使用getView(@viewId).setonClickListener(),并在回调中dismiss
     *
     * @param viewId
     * @param onClickListener
     */
    public void setOnClickListenerWithDismiss(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
    }

    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setText(int viewId, int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
    }

    public void setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
    }

    public void setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    public void setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
    }
}
