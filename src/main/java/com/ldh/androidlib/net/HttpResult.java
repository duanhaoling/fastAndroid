package com.ldh.androidlib.net;

/**
 * * 标准数据格式
 * Created by ldh on 2017/8/14.
 */

public class HttpResult<T> {
    public int state;
    public String message;
    public T data;

    public boolean isStatusOk() {
        return true;
    }
}
