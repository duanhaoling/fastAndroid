package com.ldh.androidlib.net;

import android.support.annotation.NonNull;

import com.ldh.androidlib.net.config.ApiUtil;
import com.ldh.androidlib.net.config.HttpResult;
import com.ldh.androidlib.net.observer.HttpObserver;

import io.reactivex.Observable;

import static com.ldh.androidlib.net.RetrofitFactory.createService;
import static com.ldh.androidlib.net.RetrofitFactory.createTestService;


/**
 * Created by ldh on 2017/8/16.
 * 每个请求对象对应一个新的实例
 * <p>
 * T 如果替换为别的网络框架，创建相应的service，更换这里的RetrofitFactory
 * V 需要返回的数据结构
 */

public class HttpProvider<T, V> {
    private T service;
    private Class<T> clazz;
    private ServerPoxy<T, V> poxy;

    private HttpProvider(Class<T> clazz, ServerPoxy<T, V> poxy) {
        this.clazz = clazz;
        this.poxy = poxy;
    }


    /**
     * @param clazz Service接口对应的Class
     * @param poxy  获取数据操作
     * @param <T>   Service接口
     * @param <V>   返回的数据类型
     * @return
     */
    public static <T, V> HttpProvider<T, V> newInstance(Class<T> clazz, ServerPoxy<T, V> poxy) {
        return new HttpProvider<>(clazz, poxy);
    }

    public HttpProvider<T, V> setServiceV1() {
        service = createService(clazz, "", ApiUtil.API_VER_1);
        return this;
    }

    public HttpProvider<T, V> setServiceV2() {
        service = createService(clazz, "", ApiUtil.API_VER_2);
        return this;
    }

    public HttpProvider<T, V> setServiceV3() {
        service = createService(clazz, "", ApiUtil.API_VER_3);
        return this;
    }

    public HttpProvider<T, V> baseUrl(@NonNull String baseUrl) {
        service = createService(clazz, baseUrl, "");
        return this;
    }

    /**
     * 测试接口使用，不含公共参数与header
     *
     * @param baseUrl
     * @return
     */
    public HttpProvider<T, V> testApi(@NonNull String baseUrl) {
        service = createTestService(clazz, baseUrl, "");
        return this;
    }

    public HttpProvider<T, V> excuteLoadData(HttpObserver<V> observer) {
        if (service == null) {
            service = createService(clazz); //如果没有设置，执行默认初始化
        }
        Observable<HttpResult<V>> data = poxy.getHttpObservabe(service);
        RetrofitFactory.createDatas(data, observer);
        return this;
    }

    /**
     * 使用代理类，执行实际请求，方便链式调用
     *
     * @param <T>
     * @param <V>
     */
    public interface ServerPoxy<T, V> {
        Observable<HttpResult<V>> getHttpObservabe(T service);
    }


}
