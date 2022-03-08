package com.example.androiddevapp.viewbase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import androidx.annotation.Nullable;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: ViewBase
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/3/4 11:33 上午
 */
public class ViewBase extends View {

    private Scroller mScroller;
    private Paint mPaint;
    private static final String TAG = "ViewBase";
    private GestureDetector gestureDetector;

    public ViewBase(Context context) {
        super(context);
        initData(context);
    }

    public ViewBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public ViewBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    public ViewBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(context);
    }

    private void initData(Context context) {
        mScroller = new Scroller(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        Log.w(TAG, "initData:最小滑动距离: " + ViewConfiguration.get(context).getScaledTouchSlop());
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                Log.w(TAG, "onDown: ");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
                Log.w(TAG, "onShowPress: ");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                Log.w(TAG, "onSingleTapUp: ");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                Log.w(TAG, "onScroll: ");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                Log.w(TAG, "onLongPress: ");
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                Log.w(TAG, "onFling: ");
                return false;
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.w(TAG, "left: " + getLeft()); // 0
        Log.w(TAG, "right: " + getRight()); // 1176
        Log.w(TAG, "top: " + getTop()); // 0
        Log.w(TAG, "bottom: " + getBottom()); // 2160
        Log.w(TAG, "x: " + getX()); // 0
        Log.w(TAG, "y: " + getY()); // 0
        Log.w(TAG, "translationX: " + getTranslationX()); // 0
        Log.w(TAG, "translationY: " + getTranslationY()); // 0
        Log.w(TAG, "translationZ: " + getTranslationZ()); // 0
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(100, 150, 100));
        canvas.drawRect(30, 30, 300, 300, mPaint);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int dx = destX - scrollX;
        int dy = destY - scrollY;
        Log.w(TAG, "smoothScrollTo: scrollX：" + scrollX);
        Log.w(TAG, "smoothScrollTo: scrollY：" + scrollY);
        Log.w(TAG, "smoothScrollTo: dx：" + dx);
        Log.w(TAG, "smoothScrollTo: dy：" + dy);
        mScroller.startScroll(scrollX, scrollY, dx, dy, 3000);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w(TAG, "onTouchEvent: " + event.getAction());
        Log.w(TAG, "onTouchEvent: getX()" + event.getX());
        Log.w(TAG, "onTouchEvent: getY()" + event.getY());
        Log.w(TAG, "onTouchEvent: getRawX()" + event.getRawX());
        Log.w(TAG, "onTouchEvent: getRawY()" + event.getRawY());
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        velocityTracker.computeCurrentVelocity(1000);
        float xVelocity = velocityTracker.getXVelocity();
        float yVelocity = velocityTracker.getYVelocity();
        Log.w(TAG, "水平速度: " + xVelocity + ",垂直速度：" + yVelocity);
        velocityTracker.clear();
        velocityTracker.recycle();

        boolean consume = gestureDetector.onTouchEvent(event);
//        return consume;

        smoothScrollTo(-((int) event.getX()), -((int) event.getY()));
        return true;
//        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(300, 300);
    }
}
