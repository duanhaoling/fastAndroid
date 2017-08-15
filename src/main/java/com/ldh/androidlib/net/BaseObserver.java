package com.ldh.androidlib.net;

import android.util.Log;

import com.ldh.androidlib.net.exception.ApiException;
import com.ldh.androidlib.utils.DevUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by ldh on 2017/8/15.
 */

public abstract class BaseObserver<T> implements Observer<T> {

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
            onError((ApiException) e);
        } else {
            onError(new ApiException(e, 123));
        }
    }

    @Override
    public void onComplete() {

    }

    public Disposable getDisposable() {
        return disposable;
    }

    public abstract void onError(@NonNull ApiException ex);
}
