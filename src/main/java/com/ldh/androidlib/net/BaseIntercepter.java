package com.ldh.androidlib.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by ldh on 2017/8/15.
 * 添加请求头与公共参数
 */

public class BaseIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
