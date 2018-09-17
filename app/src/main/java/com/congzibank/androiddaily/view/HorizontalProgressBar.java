package com.congzibank.androiddaily.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * 水平 进度 条
 * Created by cong_wang on 2018/9/14.
 */

public class HorizontalProgressBar extends View {
    /**
     * 进度条背景颜色
     */
    private int bgColor = 0xFFe1e5e8;
    /**
     * 进度条颜色
     */
    private int progressColor = 0xFFf66b12;

    private Paint progressPaint;
    private Paint bgPaint;
    private Paint tipPaint;
    private Paint textPaint;

    /**
     * 进度
     */
    private float progress;

    private float currentProgress = 0;

    private RectF rectF = new RectF();

    private Rect textRect = new Rect();

    private int width;
    private int height;
    /**
     * 进度条移动的距离
     */
    private int moveDistense;
    /**
     * 进度条宽度
     */
    /**
     * 绘制三角形的path
     */
    private Path path = new Path();
    private int progressWidth = ConvertUtils.dp2px(4);
    private int tipHeight = ConvertUtils.dp2px(15);
    private int tipWidth = ConvertUtils.dp2px(30);
    private int tipPaintWidth = ConvertUtils.dp2px(1);
    private int triangleHeight = ConvertUtils.dp2px(3);
    private int roundRectRadius = ConvertUtils.dp2px(2);
    private int textPaintSize = ConvertUtils.sp2px(10);
    private int progrossMaginTop = ConvertUtils.dp2px(8);

    public HorizontalProgressBar(Context context) {
        super(context);
        init();
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    /**
     * 初始化方法
     */
    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStrokeWidth(progressWidth);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(progressWidth);

        tipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tipPaint.setStrokeWidth(tipPaintWidth);
        tipPaint.setColor(progressColor);
        tipPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textPaintSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        canvas.drawLine(getPaddingLeft(), tipHeight + progrossMaginTop,
                getWidth(), tipHeight + progrossMaginTop, bgPaint);
        //画真实进度
        canvas.drawLine(getPaddingLeft(), tipHeight + progrossMaginTop,
                currentProgress, tipHeight + progrossMaginTop, progressPaint);
        //画tip
        drawTips(canvas);
        //绘制文字
        drawText(canvas);
    }

    /**
     * 绘制上面的tips
     * @param canvas
     */
    private void drawTips(Canvas canvas) {
        //先画圆角矩形
        rectF.set(moveDistense,0,moveDistense + tipWidth, tipHeight);
        canvas.drawRoundRect(rectF, roundRectRadius, roundRectRadius, tipPaint);

        //再画三角形
        path.moveTo(tipWidth / 2 + moveDistense - triangleHeight, tipHeight);
        path.lineTo(tipWidth / 2  + moveDistense, tipHeight + triangleHeight );
        path.lineTo(tipWidth / 2 + moveDistense + triangleHeight, tipHeight);
        canvas.drawPath(path, tipPaint);
        path.reset();
    }

    private String textString = "0";
    private void drawText(Canvas canvas) {
        textRect.set(moveDistense, 0, moveDistense + tipWidth, tipHeight);
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();

        int baseLine = (textRect.bottom + textRect.top - fontMetricsInt.bottom - fontMetricsInt.top) / 2;
        canvas.drawText(textString + "%", textRect.centerX(), baseLine, textPaint);
    }

    /**
     * 设置进度条带动画效果
     *
     * @param progress
     * @return
     */
    public HorizontalProgressBar setProgressWithAnimation(float progress) {
        this.progress = progress;
        initAnimation();
        return this;
    }

    private long duration = 4000;
    private long startDelay = 2000;
    /**
     * 开启动画
     */
    private void initAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, progress);
        animator.setDuration(duration);
        animator.setStartDelay(startDelay);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //得到执行比例
                float value = (float) animation.getAnimatedValue();
                currentProgress = value * width / 100;
                textString = String.valueOf((int)value);

                if (currentProgress >= (tipWidth / 2) && currentProgress <= (width - tipWidth / 2)){
                    moveDistense = (int) (currentProgress - tipWidth / 2);
                }
                invalidate();
            }
        });
        animator.start();
    }

    public static int format2Int(double i) {
        return (int) i;
    }
}
