package com.ldh.androidlib.net.exception;

/**
 * Created by ldh on 2017/8/14.
 */

public class ApiException extends Exception{
    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }
}
