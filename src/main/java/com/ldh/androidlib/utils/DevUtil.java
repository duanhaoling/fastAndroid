package com.ldh.androidlib.utils;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ldh.androidlib.BuildConfig;


/**
 * 调试工具类, 功能如下: <br/>
 * 1. log输出 <br/>
 * 2. 开启/禁用StrictMode <br/>
 * 3. 版本判断 <br/>
 * 4. 判断是否可调试 <br/>
 * 5. 判断是否运行在模拟器上 <br/>
 */
public final class DevUtil {
    public static final String TAG = "_devUtil_";
    private static final boolean isDebug = BuildConfig.DEBUG;

    private DevUtil() {
        //no instance
    }

    /**
     * 使用系统TAG输出调试信息
     * @param msg
     */
    public static void dd(String msg) {
        d(TAG, msg);
    }

    /**
     * Send a {@link Log#VERBOSE} log errorMsg.
     * @param tag Used to identify the source of a log errorMsg.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The errorMsg you would like logged.
     */
    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg + " - tag:" + tag);
        }
    }

    /**
     * Send a {@link Log#WARN} log errorMsg and log the exception.
     * @param tag Used to identify the source of a log errorMsg.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The errorMsg you would like logged.
     * @param e An exception to log
     */
    public static void w(String tag, String msg, Throwable e) {
        if (isDebug) {
            Log.w(tag, msg + " - tag:" + tag, e);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg + " - tag:" + tag);
    }

    public static void e(String tag, String msg, Throwable e) {
        if(isDebug) {
            Log.e(tag, msg, e);
        }
    }

    /**
     * 通过签名判断是否为开发版.
     * 开发版keystore见项目根目录的debug.keystore
     * 线上版的keystore为根目录下的keystore
     */
    private static boolean debuggable(Context appContext) {
        final int DEBUG_SIGNATURE_HASH = -545290802;
        final int ONLINE_SIGNATURE_HASH = -972500024;

        // 判断是否为调试状态
        // http://stackoverflow.com/questions/3029819/android-automatically-choose-debug-release-maps-api-key
        PackageManager manager = appContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(appContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature sig : info.signatures) {
                int sigHashCode = sig.hashCode();
                switch(sigHashCode) {
                    case DEBUG_SIGNATURE_HASH:
                        return true;
                    case ONLINE_SIGNATURE_HASH:
                        return false;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Send a {@link Log#DEBUG} log errorMsg.
     * @param tag Used to identify the source of a log errorMsg.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The errorMsg you would like logged.
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg + " - tag:" + tag);
        }
    }

    /**
     * 是否可调试
     * @return true-可以调试, false-不可调试
     */
    public static boolean isDebug() {
        return isDebug;
    }

    /**
     * 判断是否模拟器。如果返回TRUE，则当前是模拟器
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = null;
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            imei = tm.getDeviceId();
        }
        return imei == null || imei.equals("000000000000000");
    }

    /**
     * 开启StrickMode
     */
    @TargetApi(9)
    public static void enableStrictMode() {
        if (hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    /**
     * 关闭StrickMode
     */
    @TargetApi(9)
    public static void disableStrictMode() {
        if (hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .permitAll()
                            .penaltyLog();

            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        }
    }

    /**
     * 手机操作系统是否>= level5 2.0
     *
     * @return
     */
    public static boolean hasAndroid2_0() {
        return Build.VERSION.SDK_INT >= 5;// Build.VERSION_CODES.ECLAIR;
    }

    /**
     * 手机操作系统是否>= level7 2.1
     *
     * @return
     */
    public static boolean hasAndroid2_1() {
        return Build.VERSION.SDK_INT >= 7;// Build.VERSION_CODES.ECLAIR_MR1
    }

    /**
     * 手机操作系统是否>=Froyo level8 2.2
     *
     * @return
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= 8;// Build.VERSION_CODES.FROYO;
    }

    /**
     * 手机操作系统是否>=Gingerbread level9 2.3.1
     *
     * @return
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;// 低版本Build.VERSION_CODES.GINGERBREAD未定义
    }

    /**
     * 手机操作系统是否>=Honeycomb level11 3.0
     *
     * @return
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;// Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level12 3.1
     *
     * @return
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= 12;// Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * 手机操作系统是否>=HoneycombMR2 level12 3.2
     *
     * @return
     */
    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= 13;// Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level14 4.0
     *
     * @return
     */
    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= 14;// Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level16 4.1
     *
     * @return
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16;// Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * 手机操作系统是否>=HoneycombMR1 level17 4.2
     *
     * @return
     */
    public static boolean hasJellyBean4_2() {
        return Build.VERSION.SDK_INT >= 17;// Build.VERSION_CODES.JELLY_BEAN;
    }
}
