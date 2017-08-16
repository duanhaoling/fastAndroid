package com.ldh.androidlib.net.observer;

import android.util.Log;

import com.ldh.androidlib.net.config.Authable;
import com.ldh.androidlib.net.config.UriUtils;
import com.ldh.androidlib.net.exception.ApiException;
import com.ldh.androidlib.net.exception.ERROR;
import com.ldh.androidlib.utils.CommonUtil;
import com.ldh.androidlib.utils.DevUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by ldh on 2017/8/15.
 */

public abstract class HttpObserver<T> implements Observer<T> {

    private Disposable disposable;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
    }


    @Override
    public void onError(@NonNull Throwable e) {
        DevUtil.e(UriUtils.TAG, "onError:" + e.toString());
        DevUtil.e(UriUtils.TAG, Log.getStackTraceString(e));
//        e.printStackTrace();
        if (e instanceof ApiException) {
            ApiException ex = (ApiException) e;
            if (ex.getCode() == ERROR.TOKEN_ERROR && CommonUtil.sAppContext instanceof Authable) {
                //token过期，登出
                Authable authable = (Authable) CommonUtil.sAppContext;
                authable.onAuthTokenFailed(ex.getErrorMsg());
                return;
            }
            onError(ex);
        } else {
            onError(new ApiException(e, 123));
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 取消回调
     */
    public void unsubscribe() {
        if (!disposable.isDisposed())
            disposable.dispose();
    }

    public abstract void onError(@NonNull ApiException ex);
}
