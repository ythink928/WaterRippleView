package com.allenyu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.View;

import com.allenyu.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 水波纹
 * 水波纹色值：8EC4E6  7.5%透明度，白色2px描边  透明度30%
 * 每隔一秒 画一个圆，每个圆放大四秒
 */
public class WaterRippleView extends View {

    private static final int MAX_CIRCLE_NUM = 4;
    private static final long DRAW_DURATION_MS = 3*1000;//画一个圈4s

    Choreographer.FrameCallback mFrameCallback;
    private List<Integer> startWidthList = new ArrayList<>();

    private static final float mStartAlpha = 150f;
    private static final int mStartWidth = 0;
    private int mWidthSpeed;

    public WaterRippleView(Context context) {
        this(context,null);
    }

    public WaterRippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaterRippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0x8EC4E6);

        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setStyle(Paint.Style.STROKE);
        mWhitePaint.setStrokeWidth(DensityUtil.dp2px(1));
        mWhitePaint.setColor(0x13ffffff);

        mFrameCallback = new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                drawNext();
            }
        };
        startWidthList.add(mStartWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //  获取圆心位置
        cx = getWidth() / 2;
        cy = getHeight() / 2;

        mWidthSpeed = (int) (getMaxRadius() * 16/DRAW_DURATION_MS);
    }

    public void start(){
        drawNext();
    }

    public void stop(){
        Choreographer.getInstance().removeFrameCallback(mFrameCallback);
    }

    private void drawNext(){
        invalidate();
        Choreographer.getInstance().postFrameCallback(mFrameCallback);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    float cx,cy;
    Paint mPaint;
    Paint mWhitePaint;


    private int getMaxRadius(){
        if (getWidth()>getHeight()){
            return getHeight()/2;
        }
        return getWidth()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明

        for (int i=0;i<startWidthList.size();i++){

            int startWidth = startWidthList.get(i);
            //根据半径来计算alpha值
            float alpha = mStartAlpha - startWidth*mStartAlpha/getMaxRadius();
            mPaint.setAlpha((int) alpha);
            mWhitePaint.setAlpha((int) alpha);
            //画圆
            canvas.drawCircle(cx,cy,startWidth,mPaint);

            //画圆环
            canvas.drawCircle(cx,cy,startWidth,mWhitePaint);

            // 同心圆扩散
            if (alpha > 0 && startWidth < getMaxRadius()) {
                startWidthList.set(i, (startWidth + mWidthSpeed));
            }
        }

        //上一个大小达到5分之1则增加一个圆
        int addRadius = getMaxRadius() / MAX_CIRCLE_NUM;
        if (startWidthList.get(startWidthList.size() - 1) > addRadius) {
            startWidthList.add(mStartWidth);
        }

        //最多同时显示5个
        if (startWidthList.size() > MAX_CIRCLE_NUM){
            startWidthList.remove(0);
        }
    }

}
