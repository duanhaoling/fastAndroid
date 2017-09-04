package com.ldh.androidlib.image.frescoloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.facebook.imagepipeline.request.BasePostprocessor;
import com.ldh.androidlib.utils.BitmapUtils;

/**
 * Created by ldh on 2017/9/4.
 */

public class BlurPostprocessor extends BasePostprocessor {
    private float mRadius;
    private Context context;

    public BlurPostprocessor(Context context, float blurRadius) {
        this.mRadius = blurRadius;
        this.context=context;
    }

    @Override
    public void process(Bitmap destBitmap, Bitmap sourceBitmap) {
        Bitmap result=null;
        try {
            result= BitmapUtils.fastBlur(context, sourceBitmap, 15);
        } catch (Exception e) {
            e.printStackTrace();
            result=sourceBitmap;
        }finally {
            super.process(destBitmap, result);
        }
    }

    @Override
    public void process(Bitmap bitmap) {
        try {
            try {
                Bitmap result = BitmapUtils.fastBlur(context, bitmap, 15);
                super.process(result);
            } catch (OutOfMemoryError e) {
                Log.e("imageloader","OOM ...");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return "FastBlurPostprocessor";
    }
}
