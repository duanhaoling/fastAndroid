package com.ldh.androidlib.net;

/**
 * Created by ldh on 2017/8/15.
 */

class ApiUtil {
    public static String sHost;

    public static final String weatherUrl = "http://apis.baidu.com/apistore/weatherservice/citylist";
    public static final String appkey = "d51a3b6299a0313081653f4f54d80586";

    static {
        sHost = weatherUrl;
    }

    public static String getHost() {
        return sHost;
    }

    public static String getVersion() {
        return "";
    }

    public static String getBaseUrl() {
        return "";
    }
}
