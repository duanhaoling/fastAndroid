package com.ldh.androidlib.net.exception;

/**
 * 和服务端的约定异常
 * Created by ldh on 2017/8/14.
 */

public class ERROR {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络错误
     */
    public static final int NETWORD_ERROR = 1002;
    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = 1003;
    /**
     * token过期
     */
    public static final int TOKEN_ERROR = 1004;
}
