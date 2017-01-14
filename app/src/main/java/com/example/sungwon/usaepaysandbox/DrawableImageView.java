package com.example.sungwon.usaepaysandbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawableImageView extends View{
    private List<Path> mGraphics = new ArrayList<Path>();
    private Path mPath;
    private Paint mPaint;

    public DrawableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path path : mGraphics) {
            canvas.drawPath(path, mPaint);
        }
        canvas.save();
    }

    public void clearView() {
        mGraphics = new ArrayList<Path>();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mPath = new Path();
            mGraphics.add(mPath);
            mPath.moveTo(event.getX(), event.getY());
            mPath.lineTo(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mPath.lineTo(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mPath.lineTo(event.getX(), event.getY());
        }

        invalidate();

        return true;
    }
}
