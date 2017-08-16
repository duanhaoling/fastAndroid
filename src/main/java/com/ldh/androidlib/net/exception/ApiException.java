package com.ldh.androidlib.net.exception;

/**
 * Created by ldh on 2017/8/14.
 */

public class ApiException extends Exception {
    private int code;
    private String errorMsg;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
