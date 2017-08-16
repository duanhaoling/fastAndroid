package com.ldh.androidlib.utils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by ldh on 2017/8/16.
 */

public abstract class AbstractSingleton<T> {
    private final AtomicReference<T> ref = new AtomicReference<>();

    public T get() {
        T ret = ref.get();
        if (ret == null) {
            synchronized (this) {
                if (ref.get() == null) {
                    ret = newInstance();
                    ref.set(ret);
                } else {
                    ret = ref.get();
                }
            }
        }
        return ret;
    }

    protected abstract T newInstance();
}
