package com.congzibank.androiddaily.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 带有进度的imageview
 * 用来模拟文件上传时的进度，可见微信文件上传的效果
 */
public class ProgressImageView extends AppCompatImageView {

    private int totalProgress = 100;

    private float currentProgress = 0;
    private Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public ProgressImageView(Context context) {
        this(context, null);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        shadowPaint.setColor(Color.GRAY);
        shadowPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(- getHeight() / 2,
                -getWidth() / 2,getWidth() + getHeight() / 2,
                getHeight() + getWidth() / 2,
                -90 + currentProgress / totalProgress * 360, 360 - 360 * currentProgress / totalProgress, true, shadowPaint );
    }

    public void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, totalProgress);
        animator.setStartDelay(2000);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator.start();
    }

    public void setProgress(float progress) {
        this.currentProgress = progress;
        invalidate();
    }
}
