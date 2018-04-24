package com.ldh.androidlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author ldh
 * @date 2018/3/16
 */
public class UIUtils2 {
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 10000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 是否隐藏软键盘 true表示隐藏，false表示不隐藏
     *
     * @param isHide
     */
    public static void isHideKeyBoard(View view, boolean isHide) {
        view.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (isHide) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * dip2px:dip值转pix值. <br/>
     *
     * @param context
     * @param dipValue
     * @return 转化后的pix值
     * @author zhihuxing@anjuke.com
     * @since Ver 4.1
     */
    public static float dip2px(Context context, float dipValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources()
                .getDisplayMetrics());
    }

    /**
     * sp值转换为px值
     *
     * @param context
     * @param psValue
     * @return
     */
    public static float sp2px(Context context, float psValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, psValue, context.getResources()
                .getDisplayMetrics());
    }

    /**
     * dip2px:pix值转dip值. <br/>
     *
     * @param context
     * @param pxValue
     * @return 转化后的dp值
     * @author zhihuxing@anjuke.com
     * @since Ver 4.1
     */
    public static float px2dip(Context context, float pxValue) {
        return pxValue / context.getResources().getDisplayMetrics().density;
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 样式：A-B-A型
     *
     * @param context
     * @param string      字符串
     * @param first       首部分字符个数
     * @param last        末部分开始下标
     * @param middleSize  中间字大小
     * @param otherSize   两边字大小
     * @param middleColor 中间字颜色
     * @param otherColor  两边字颜色
     * @return
     */
    public static CharSequence setMiddleTextColor(Context context, String string, int first, int last,
                                                  float middleSize, float otherSize, int middleColor, int otherColor) {
        if (context == null)
            return "";
        final SpannableStringBuilder sb = new SpannableStringBuilder(string);
        sb.setSpan(new AbsoluteSizeSpan((int) dip2px(context, otherSize)), 0, first,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan((int) dip2px(context, middleSize)), first, sb.length() - last,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan((int) dip2px(context, otherSize)), sb.length() - last, sb.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(otherColor), 0, first, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(middleColor), first, sb.length() - last, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(otherColor), sb.length() - last, sb.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    /**
     * 样式：A-B型
     *
     * @param context
     * @param string
     * @param index
     * @param leftSize
     * @param rightSize
     * @param leftColor
     * @param rightColor
     * @return
     */
    public static CharSequence setTwinsCharSequence(Context context, String string, int index, float leftSize, float rightSize, int leftColor, int rightColor) {
        if (context == null) {
            return "";
        }
        final SpannableStringBuilder sb = new SpannableStringBuilder(string);
        sb.setSpan(new AbsoluteSizeSpan((int) dip2px(context, leftSize)), 0, index,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan((int) dip2px(context, rightSize)), index, sb.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(leftColor), 0, index, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(rightColor), index, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    public static View inflate(Context context, int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) return;
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
