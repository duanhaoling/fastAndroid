package com.ldh.androidlib.image;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.view.View;

import com.ldh.androidlib.image.glideloader.GlideImageLoader;

/**
 * Created by ldh on 2017/9/4.
 */
@Keep
public class ImageLoaderManager implements IImageLoaderstrategy {
    private static final ImageLoaderManager INSTANCE = new ImageLoaderManager();
    private IImageLoaderstrategy loaderstrategy;

    private ImageLoaderManager() {
    }

    public static ImageLoaderManager getInstance() {
        return INSTANCE;
    }

    public void setImageLoaderStrategy(IImageLoaderstrategy strategy) {
        loaderstrategy = strategy;
    }

    /*
     *   可创建默认的Options设置，假如不需要使用ImageView ，
     *    请自行new一个Imageview传入即可
     *  内部只需要获取Context
     */
    public static ImageLoaderOptions getDefaultOptions(@NonNull View container, @NonNull String url) {
        return new ImageLoaderOptions.Builder(container, url).isCrossFade(true).build();
    }

    @Override
    public void showImage(@NonNull ImageLoaderOptions options) {
        if (loaderstrategy != null) {
            loaderstrategy.showImage(options);
        }
    }

    public void showImage(@NonNull View container, @NonNull String url) {
        ImageLoaderOptions options = getDefaultOptions(container, url);
        showImage(options);
    }

    @Override
    public void hideImage(@NonNull View view, int visiable) {
        if (loaderstrategy != null) {
            loaderstrategy.hideImage(view, visiable);
        }
    }


    @Override
    public void cleanMemory(Context context) {
        loaderstrategy.cleanMemory(context);
    }

    @Override
    public void pause(Context context) {
        if (loaderstrategy != null) {
            loaderstrategy.pause(context);
        }
    }

    @Override
    public void resume(Context context) {
        if (loaderstrategy != null) {
            loaderstrategy.resume(context);
        }
    }

    // 在application的oncreate中初始化
    @Override
    public void init(Context context) {
//        loaderstrategy = new FrescoImageLoader();
        loaderstrategy=new GlideImageLoader();
        loaderstrategy.init(context);
    }


}
