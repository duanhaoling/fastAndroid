package com.ldh.androidlib.net.exception;

/**
 * Created by ldh on 2017/8/14.
 */

public class ServerException extends RuntimeException {
    public int code;
    public String message;

    public ServerException(int state, String message) {
        this.code = state;
        this.message = message;
    }
}
