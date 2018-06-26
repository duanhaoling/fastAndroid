package com.ldh.androidlib.net;

import com.ldh.androidlib.net.config.HttpResult;
import com.ldh.androidlib.net.exception.ExceptionEngine;
import com.ldh.androidlib.net.exception.ServerException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ldh on 2017/8/14.
 */

public class Transformers {

    /**
     * 判断返回码，抛出异常或者取出数据 ，并交给ExceptionEngine处理异常
     * map()  判断返回状态码，取出data，保证不为空。
     * onErrorResumeNext()  通过ExceptionEngine对网络异常和服务端返回异常分类，统一为ErrorInfo
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<HttpResult<T>, T> dataTransformer() {
        return new ObservableTransformer<HttpResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<HttpResult<T>> upstream) {
                return upstream.map(new Function<HttpResult<T>, T>() {
                    @Override
                    public T apply(@NonNull HttpResult<T> tResponse) throws Exception {
                        if (!tResponse.isStatusOk()) {
                            throw new ServerException(tResponse.state, tResponse.message);
                        }
                        return tResponse.data;
                    }
                }).onErrorResumeNext(new HttpResponseFunc<T>());  //处理异常
            }
        };
    }

    /**
     * 线程调度
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> switchSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 交给ExceptionEngine处理异常
     *
     * @param <T>
     */
    private static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {

        @Override
        public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
            //ExceptionEngine为处理异常的驱动器
            return Observable.error(ExceptionEngine.handleException(throwable));
        }
    }


}
