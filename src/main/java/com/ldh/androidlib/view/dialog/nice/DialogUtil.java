package com.ldh.androidlib.view.dialog.nice;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by ldh on 2017/9/8.
 */

public class DialogUtil {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
}
