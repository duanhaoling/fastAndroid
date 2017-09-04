package com.ldh.androidlib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;

import javax.microedition.khronos.egl.EGL10;

/**
 * Created by ldh on 2017/8/31.
 */

class GmacsEnvi {
    public static Context appContext;
    public static String androidId;
    public static String deviceId;
    public static String wifiMac;
    public static int screenWidth;
    public static int screenHeight;
    public static int statusBarHeight;
    public static int navigationBarHeight;
    public static float density;
    private static int a;

    public GmacsEnvi() {
    }

    public static void initialize(Context context) {
        appContext = context.getApplicationContext();
        DisplayMetrics var1 = appContext.getResources().getDisplayMetrics();
        androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        TelephonyManager var2 = (TelephonyManager) context.getSystemService("phone");
        if (0 == PermissionChecker.checkSelfPermission(context, "android.permission.READ_PHONE_STATE")) {
            deviceId = var2.getDeviceId();
        }

        WifiManager manager = (WifiManager) context.getSystemService("wifi");
        wifiMac = manager.getConnectionInfo().getMacAddress();
        screenWidth = var1.widthPixels;
        screenHeight = var1.heightPixels;
        statusBarHeight = getStatusBarHeight();
        navigationBarHeight = getNavigationBarHeight();
        density = var1.density;
    }

    public static int getGLMaxTextureSize() {
        if (a == 0) {
            int[] var2;
            int[] var10;
            if (Build.VERSION.SDK_INT >= 17) {
                EGLDisplay var0 = EGL14.eglGetDisplay(0);
                int[] var1 = new int[2];
                EGL14.eglInitialize(var0, var1, 0, var1, 1);
                var2 = new int[]{12351, 12430, 12329, 0, 12352, 4, 12339, 1, 12344};
                EGLConfig[] var3 = new EGLConfig[1];
                int[] var4 = new int[1];
                EGL14.eglChooseConfig(var0, var2, 0, var3, 0, 1, var4, 0);
                EGLConfig var5 = var3[0];
                int[] var6 = new int[]{12375, 64, 12374, 64, 12344};
                EGLSurface var7 = EGL14.eglCreatePbufferSurface(var0, var5, var6, 0);
                int[] var8 = new int[]{12440, 2, 12344};
                EGLContext var9 = EGL14.eglCreateContext(var0, var5, EGL14.EGL_NO_CONTEXT, var8, 0);
                EGL14.eglMakeCurrent(var0, var7, var7, var9);
                var10 = new int[1];
                GLES20.glGetIntegerv(3379, var10, 0);
                EGL14.eglMakeCurrent(var0, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroySurface(var0, var7);
                EGL14.eglDestroyContext(var0, var9);
                EGL14.eglTerminate(var0);
                a = var10[0];
            } else {
                EGL10 var13 = (EGL10) javax.microedition.khronos.egl.EGLContext.getEGL();
                javax.microedition.khronos.egl.EGLDisplay var14 = var13.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
                var2 = new int[2];
                var13.eglInitialize(var14, var2);
                int[] var15 = new int[]{12351, 12430, 12329, 0, 12339, 1, 12344};
                javax.microedition.khronos.egl.EGLConfig[] var16 = new javax.microedition.khronos.egl.EGLConfig[1];
                int[] var17 = new int[1];
                var13.eglChooseConfig(var14, var15, var16, 1, var17);
                javax.microedition.khronos.egl.EGLConfig var18 = var16[0];
                int[] var19 = new int[]{12375, 64, 12374, 64, 12344};
                javax.microedition.khronos.egl.EGLSurface var20 = var13.eglCreatePbufferSurface(var14, var18, var19);
                boolean var21 = true;
                var10 = new int[]{12440, 1, 12344};
                javax.microedition.khronos.egl.EGLContext var11 = var13.eglCreateContext(var14, var18, EGL10.EGL_NO_CONTEXT, var10);
                var13.eglMakeCurrent(var14, var20, var20, var11);
                int[] var12 = new int[1];
                GLES10.glGetIntegerv(3379, var12, 0);
                var13.eglMakeCurrent(var14, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                var13.eglDestroySurface(var14, var20);
                var13.eglDestroyContext(var14, var11);
                var13.eglTerminate(var14);
                a = var12[0];
            }

            if (a == 0) {
                a = 4096;
            }
        }

        return a;
    }

    /**
     * 状态栏
     * @return
     */
    private static int getStatusBarHeight() {
        Resources var0 = appContext.getResources();
        int var1 = var0.getIdentifier("status_bar_height", "dimen", "android");
        return var0.getDimensionPixelSize(var1);
    }

    private static int getNavigationBarHeight() {
        if (isShowNavigationBar()) {
            Resources var0 = appContext.getResources();
            int var1 = var0.getIdentifier("navigation_bar_height", "dimen", "android");
            return var0.getDimensionPixelSize(var1);
        } else {
            return 0;
        }
    }

    //屏幕下方的三个虚拟按键栏
    private static boolean isShowNavigationBar() {
        boolean var0 = false;
        Resources var1 = appContext.getResources();
        int var2 = var1.getIdentifier("config_showNavigationBar", "bool", "android");
        if (var2 > 0) {
            var0 = var1.getBoolean(var2);
        }

        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", new Class[]{String.class});
            String str = (String) method.invoke(clazz, new Object[]{"qemu.hw.mainkeys"});
            if ("1".equals(str)) {
                var0 = false;
            } else if ("0".equals(str)) {
                var0 = true;
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return var0;
    }
}
