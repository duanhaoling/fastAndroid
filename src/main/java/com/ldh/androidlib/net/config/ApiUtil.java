package com.ldh.androidlib.net.config;

/**
 * Created by ldh on 2017/8/15.
 */

public class ApiUtil {

    public static String sHost;
    public static final String API_VER_1 = "/1.0/";
    public static final String API_VER_2 = "/2.0/";
    public static final String API_VER_3 = "/3.0/";
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
