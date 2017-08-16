package com.ldh.androidlib.utils;

import android.content.Context;

/**
 * Created by ldh on 2016/10/19 0019.
 */
public class CommonUtil {
    public static Context sAppContext;
    public static String sToken;
    public static String sUserId;

    /**
     * @param appContext applicaton context (not activity)
     * @param token
     * @param userId
     */
    public static void init(Context appContext, String token, String userId) {
        sAppContext = appContext;
        sToken = token;
        sUserId = userId;
    }
}
