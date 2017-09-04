package com.ldh.androidlib.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by ldh on 2017/8/31.
 */

public class BitmapUtils {

    public static Bitmap circleBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int finalSize;
        float scale;
        float dx = 0, dy = 0;
        if (width >= height) {
            finalSize = height;
            scale = (float) finalSize / (float) height;
            dx = (finalSize - width * scale) * 0.5f;
        } else {
            finalSize = width;
            scale = (float) finalSize / (float) width;
            dy = (finalSize - height * scale) * 0.5f;
        }
        Bitmap layer = Bitmap.createBitmap(finalSize, finalSize, Bitmap.Config.ARGB_8888);
        RectF rect = new RectF(0.0f, 0.0f, finalSize, finalSize);
        Canvas canvas = new Canvas(layer);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);
        shader.setLocalMatrix(matrix);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        canvas.drawRoundRect(rect, finalSize / 2, finalSize / 2, paint);
        return layer;
    }

    public static Bitmap fastBlur(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }

        final int sourceWidth = bitmap.getWidth();
        final int sourceHeight = bitmap.getWidth();
        int overlayWidth;
        int overlayHeight;
        int dx = 0;
        int dy = 0;
        if (GmacsEnvi.screenHeight * sourceWidth > GmacsEnvi.screenWidth * sourceHeight) {
            overlayHeight = sourceHeight / 8;
            overlayWidth = sourceHeight * GmacsEnvi.screenWidth / GmacsEnvi.screenHeight / 8;
            dx = (sourceWidth - sourceHeight * GmacsEnvi.screenWidth / GmacsEnvi.screenHeight) / 2;
        } else {
            overlayWidth = sourceWidth / 8;
            overlayHeight = sourceWidth * GmacsEnvi.screenHeight / GmacsEnvi.screenWidth / 8;
            dy = (sourceHeight - sourceWidth * GmacsEnvi.screenHeight / GmacsEnvi.screenWidth) / 2;
        }

        Bitmap overlay = Bitmap.createBitmap(overlayWidth, overlayHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / 8f, 1 / 8f);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, -dx, -dy, paint);
        gaussBlur(overlay, 4);
        return overlay;
    }

    private static void gaussBlur(Bitmap bitmap, int radius) {
        if (radius < 1) {
            return;
        }

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int[] bitmapPix = new int[bitmapWidth * bitmapHeight];
        bitmap.getPixels(bitmapPix, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        bitmapPix = setBitmapPixDefaultAlpha(bitmapPix);

        int yLineMaxValue = bitmapWidth - 1;
        int xLineMaxValue = bitmapHeight - 1;
        int totalPoint = bitmapWidth * bitmapHeight;
        int div = radius + radius + 1;

        int r[] = new int[totalPoint];
        int g[] = new int[totalPoint];
        int b[] = new int[totalPoint];
        int rSumWeight, gSumWeight, bSumWeight, x, y, i, pointRGB, yp, yi, yw;
        int vmin[] = new int[Math.max(bitmapWidth, bitmapHeight)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int weight;
        int maxWeight = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < bitmapHeight; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rSumWeight = gSumWeight = bSumWeight = 0;
            for (i = -radius; i <= radius; i++) {
                pointRGB = bitmapPix[yi + Math.min(yLineMaxValue, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (pointRGB & 0xff0000) >> 16;
                sir[1] = (pointRGB & 0x00ff00) >> 8;
                sir[2] = (pointRGB & 0x0000ff);
                weight = maxWeight - Math.abs(i);
                rSumWeight += sir[0] * weight;
                gSumWeight += sir[1] * weight;
                bSumWeight += sir[2] * weight;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < bitmapWidth; x++) {
                r[yi] = dv[rSumWeight];
                g[yi] = dv[gSumWeight];
                b[yi] = dv[bSumWeight];

                rSumWeight -= routsum;
                gSumWeight -= goutsum;
                bSumWeight -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, yLineMaxValue);
                }
                pointRGB = bitmapPix[yw + vmin[x]];

                sir[0] = (pointRGB & 0xff0000) >> 16;
                sir[1] = (pointRGB & 0x00ff00) >> 8;
                sir[2] = (pointRGB & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rSumWeight += rinsum;
                gSumWeight += ginsum;
                bSumWeight += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += bitmapWidth;
        }
        for (x = 0; x < bitmapWidth; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rSumWeight = gSumWeight = bSumWeight = 0;
            yp = -radius * bitmapWidth;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                weight = maxWeight - Math.abs(i);

                rSumWeight += r[yi] * weight;
                gSumWeight += g[yi] * weight;
                bSumWeight += b[yi] * weight;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < xLineMaxValue) {
                    yp += bitmapWidth;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < bitmapHeight; y++) {
                // Preserve alpha channel: ( 0xff000000 & bitmapPix[yi] )
                bitmapPix[yi] = (0xff000000 & bitmapPix[yi]) | (dv[rSumWeight] << 16) | (dv[gSumWeight] << 8) | dv[bSumWeight];

                rSumWeight -= routsum;
                gSumWeight -= goutsum;
                bSumWeight -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + maxWeight, xLineMaxValue) * bitmapWidth;
                }
                pointRGB = x + vmin[y];

                sir[0] = r[pointRGB];
                sir[1] = g[pointRGB];
                sir[2] = b[pointRGB];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rSumWeight += rinsum;
                gSumWeight += ginsum;
                bSumWeight += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += bitmapWidth;
            }
        }

        bitmap.setPixels(bitmapPix, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
    }

    private static int[] setBitmapPixDefaultAlpha(int[] pixes) {
        int length = pixes.length;
        int[] bitmapPixes = new int[length];
        for (int i = 0; i < length; i++) {
            if ((pixes[i] & 0xFFFFFF) == 0) {
                bitmapPixes[i] = 0xFFFFFFFF;
            } else {
                bitmapPixes[i] = pixes[i] | 0xFF000000;
            }
        }
        return bitmapPixes;
    }


    /**
     * 设置图片大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap bitmapSetSize(Bitmap bitmap, int width, int height) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float scaleWidth = (float) width / bitmapWidth;
        float scaleHeight = (float) height / bitmapHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        return newBitmap;
    }

    /**
     * 按比例缩放图片
     *
     * @param bitmap
     * @param prop
     * @return
     */
    public static Bitmap bitmapSetSize(Bitmap bitmap, float prop) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(prop, prop);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        return newBitmap;
    }

    /**
     * 判断图片宽高比例
     *
     * @param bitmap
     * @return
     */
    public static boolean bitmapSizeOk(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float proportion = (float) width / height;
        return proportion >= 1.5;
    }

    /**
     * @param context
     * @param bpin
     * @param prop    缩放比例
     * @param radius
     * @return
     */
    public static Bitmap fastBlur(Context context, Bitmap bpin, float prop, int radius) {
        Bitmap bp = bitmapSetSize(bpin, prop);
        return fastBlur(context, bp, radius);
    }

    /**
     * 高斯模糊算法
     *
     * @param context
     * @param sentBitmap
     * @param radius
     * @return
     */
    public static Bitmap fastBlur(Context context, Bitmap sentBitmap, int radius) {
//        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        Bitmap bitmap = sentBitmap;
        if (bitmap == null) {
            return null;
        }
        if (radius < 1) {
            return null;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
//        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
//        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
//        return (bitmap);
        return Bitmap.createBitmap(pix, 0, w, w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * 获取相片路径
     * <p>
     * 参考: http://blog.csdn.net/tempersitu/article/details/20557383
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    public static String getPhotoPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    // 获取要查询的那张图片在 MediaProvider 中的 uri.
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}
