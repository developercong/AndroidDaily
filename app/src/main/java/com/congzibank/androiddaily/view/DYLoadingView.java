package com.congzibank.androiddaily.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.concurrent.LinkedTransferQueue;

import javax.xml.parsers.FactoryConfigurationError;

/**
 * 仿照网上模仿抖音自己的实现
 * Created by cong_wang on 2018/9/17.
 */

public class DYLoadingView extends View {

    /**
     * 左右小球的默认半径大小
     */
    private int lInitRadius = ConvertUtils.dp2px(6);
    private int rInitRadius = ConvertUtils.dp2px(6);

    private float lRadius;
    private float rRadius;
    /**
     * 左右小球的缩放比例
     */
    private float lScale = 1.2f;
    private float rScale = 0.8f;
    /**
     * 开启缩放的区间
     */
    private float startFraction = 0.2f;
    private float endFraction = 0.8f;

    /**动画执行时间*/
    private int duration = 350;
    private int pauseDuration = 80;

    /**两球之间的距离*/
    private int distance = ConvertUtils.dp2px(1);

    private int lColor = 0xffff4040;
    private int rColor = 0xFF00EEEE;
    private int mixColor = Color.BLACK;

    /**左球 右球 混合时的画笔*/
    private Paint lPaint;
    private Paint rPaint;
    private Paint mixPaint;

    /**不同状态的path*/
    private Path lPath;
    private Path rPath;
    private Path mixPath;

    /**真正的距离--两个小球圆心的距离*/
    private float realDistance;

    /**动画执行的比例--（0 -- 1）*/
    private float fraction;
    public DYLoadingView(Context context) {
        this(context, null);
    }

    public DYLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DYLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        realDistance = lInitRadius + rInitRadius + distance;
        init();
    }

    private void init() {
        lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mixPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        lPaint.setColor(lColor);
        rPaint.setColor(rColor);
        mixPaint.setColor(mixColor);

        lPath = new Path();
        rPath = new Path();
        mixPath = new Path();
    }

    /**中心点的Y坐标*/
    private int centerY;
    /**初始时 两个小球的x坐标*/
    private float lCenterX;
    private float rCenterX;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerY = getMeasuredHeight() / 2;

        lCenterX = getMeasuredWidth()/ 2 - realDistance / 2;
        rCenterX = getMeasuredWidth()/ 2 + realDistance / 2;

        lCenterX = realDistance * fraction + lCenterX;
        rCenterX = rCenterX - realDistance * fraction;

        radius(fraction);
        lPath.reset();
        lPath.addCircle(lCenterX, centerY, lRadius, Path.Direction.CW);
        rPath.reset();
        rPath.addCircle(rCenterX, centerY, rRadius, Path.Direction.CW);
        mixPath.op(lPath, rPath, Path.Op.INTERSECT);

        canvas.drawPath(lPath, lPaint);
        canvas.drawPath(rPath, rPaint);
        canvas.drawPath(mixPath, mixPaint);
    }

    /**
     * 得到真正的运动中的radius
     * @param faction 动画比例
     * @return
     */
    private void radius(float faction) {
        if (faction <= startFraction) {
            float realFraction = fraction / startFraction;
            lRadius = lInitRadius * (1 + (lScale - 1) * realFraction);
            rRadius = rInitRadius * (1 + (rScale - 1) * realFraction);
        } else if (faction >= endFraction) {
            float realFraction = fraction - 1 / startFraction - 1;
            lRadius = lInitRadius * (1 + (lScale - 1) * realFraction);
            rRadius = rInitRadius * (1 + (rScale - 1) * realFraction);
        } else {
            lRadius = lInitRadius * lScale;
            rRadius = rInitRadius * rScale;
        }
    }


    private ValueAnimator animator;
    public void startAnimation() {
        fraction = 0.0f;
        stop(); //先关闭之前的动画

        animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setDuration(duration);
        if (pauseDuration > 0) {
            animator.setStartDelay(pauseDuration);
            animator.setInterpolator(new AccelerateInterpolator());
        } else {
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setInterpolator(new LinearInterpolator());
        }

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = animation.getAnimatedFraction();
                invalidate();
            }
        });
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                if (animation != null) animation.start();
//            }
//        });
        animator.start();
    }

    private void stop() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }
}
