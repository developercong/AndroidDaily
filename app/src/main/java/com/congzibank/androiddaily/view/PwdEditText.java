package com.congzibank.androiddaily.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.util.AttributeSet;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * 仿照微信 和网络上的实现方法的密码输入框
 * Created by cong_wang on 2018/9/14.
 */

public class PwdEditText extends android.support.v7.widget.AppCompatEditText {

    private int minHeight = ConvertUtils.dp2px(120);
    private int minWidth = ConvertUtils.dp2px(30);

    /** 默认的密码数量为6 */
    private int numCount = 6;

    private int PwdWith;
    private int PwdHeight;

    private int lineStartX;
    private int dotStartX;
    private int dotStartY;

    private Paint borderPaint;
    private Paint dotPaint;
    private int boderColor = Color.parseColor("#666666");
    private int rectAngle = ConvertUtils.dp2px(6);

    private int borderWidth = ConvertUtils.dp2px(1);
    private RectF rectF = new RectF();

    private int circleRadius = ConvertUtils.dp2px(3);

    private int textLength = 0;

    private onPwdListener listener;

    public onPwdListener getListener() {
        return listener;
    }

    public void setListener(onPwdListener listener) {
        this.listener = listener;
    }

    public PwdEditText(Context context) {
        super(context);
        init();
    }

    public PwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PwdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(boderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);

        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(Color.BLACK);
        setBackgroundColor(Color.TRANSPARENT);
        setCursorVisible(false);
        //设置最多输入的数量--设置了最大可输入的长度
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(numCount)});
    }

    /**
     * after measure this method will be invoke
     * we can get width height here
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        PwdHeight = h;
        PwdWith = w;
        //初始化数值
        lineStartX = w / numCount;
        dotStartX = lineStartX / 2;
        dotStartY = PwdHeight / 2;

        rectF.set(0 ,0 , PwdWith, PwdHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制边框
        canvas.drawRoundRect(rectF, rectAngle, rectAngle, borderPaint);
        //绘制竖线
        for (int i = 0 ; i < numCount - 1 ; i ++) {
            canvas.drawLine(lineStartX * (i + 1), 0 ,
                    lineStartX  * (i + 1),
                    PwdHeight,
                    borderPaint);
        }

        drawCircle(canvas);
    }

    /**
     * 绘制圆心
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        for(int i = 0 ; i < textLength; i++) {
            canvas.drawCircle(dotStartX * (2 * i + 1),
                    dotStartY,
                    circleRadius, dotPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        textLength = text.toString().length();

        if(textLength == numCount) {
            if (listener != null) {
                listener.inputFinished(text.toString());
            }
        }
}

    public interface onPwdListener {
        void onDifference(String oldString, String newString);

        void onEqual(String pwd);

        void inputFinished(String pwd);

    }

}
