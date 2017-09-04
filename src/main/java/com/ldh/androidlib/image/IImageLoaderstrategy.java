package com.ldh.androidlib.image;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by ldh on 2017/9/4.
 */

public interface IImageLoaderstrategy {
    void showImage(@NonNull ImageLoaderOptions options);

    void hideImage(@NonNull View view, int visiable);

    void cleanMemory(Context context);

    void pause(Context context);

    void resume(Context context);

    // 在application的oncreate中初始化
    void init(Context context);
}
