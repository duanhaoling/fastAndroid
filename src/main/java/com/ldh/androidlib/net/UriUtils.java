package com.ldh.androidlib.net;


import com.ldh.androidlib.utils.DateUtil;

/**
 * Created by ldh 17/8/14.
 */
public final class UriUtils {

    public static final String TAG = "OkHttp";

    public static String addCommonParams(String url, String qtime) {
        StringBuffer tempBuffer = new StringBuffer(url);
        if (url.contains("?")) {
            tempBuffer.append("&");
        } else {
            tempBuffer.append(("?"));
        }
        tempBuffer.append(getExtraParamsNoFirstAnd(qtime));
        return tempBuffer.toString();
    }

    private static String getExtraParamsNoFirstAnd(String qtime) {
        return "公共参数";
    }


    public static String getQtime() {
        return DateUtil.formatTime(System.currentTimeMillis(), "yyyyMMddHHmmss");
    }

}
