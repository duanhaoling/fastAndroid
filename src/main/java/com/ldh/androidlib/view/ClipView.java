package com.ldh.androidlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ClipView extends View {

    private Paint mPaint;

    private int wid, hei;
    private int pX, pY;

    public ClipView(Context context) {
        this(context, null);
    }

    public ClipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        pX = (width - wid) / 2;
        pY = (height - hei) / 2;
        mPaint.setColor(0x88000000);
        canvas.drawRect(0, 0, width, pY, mPaint);
        canvas.drawRect(0, pY + hei, width, height, mPaint);
        canvas.drawRect(0, pY, pX, pY + hei, mPaint);
        canvas.drawRect(pX + wid, pY, width, pY + hei, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2.0f);
        canvas.drawLine(pX, pY, width - pX, pY, mPaint);
        canvas.drawLine(pX, pY + hei, width - pX, pY + hei, mPaint);
        canvas.drawLine(pX, pY, pX, pY + hei, mPaint);
        canvas.drawLine(width - pX, pY, width - pX, pY + hei, mPaint);
    }

    public void set(int wid, int hei) {
        this.wid = wid;
        this.hei = hei;
        invalidate();
    }

}
