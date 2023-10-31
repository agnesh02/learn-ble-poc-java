package com.example.testpoc.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.ColorInt;

public class SpeedometerLite extends View {

    // Attribute Defaults
    private int maxSpeed = 60;
    private float borderSize = 36f;
    private float textGap = 50f;
    @ColorInt
    private int borderColor = Color.parseColor("#402c47");
    @ColorInt
    private int fillColor = Color.parseColor("#d83a78");
    @ColorInt
    private int textColor = Color.parseColor("#f5f5f5");
    private String metricText = "Bpm";

    // Dynamic Values
    private RectF indicatorBorderRect = new RectF();
    private RectF tickBorderRect = new RectF();
    private Rect textBounds = new Rect();
    private float angle = MIN_ANGLE;
    private int speed = 0;

    // Dimension Getters
    private float centerX() {
        return getWidth() / 2f;
    }

    private float centerY() {
        return getHeight() / 2f;
    }

    // Core Attributes
    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int value) {
        maxSpeed = value;
        invalidate();
    }

    public float getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(float value) {
        borderSize = value;
        paintIndicatorBorder.setStrokeWidth(value);
        paintIndicatorFill.setStrokeWidth(value);
        invalidate();
    }

    public float getTextGap() {
        return textGap;
    }

    public void setTextGap(float value) {
        textGap = value;
        invalidate();
    }

    public String getMetricText() {
        return metricText;
    }

    public void setMetricText(String value) {
        metricText = value;
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(@ColorInt int value) {
        borderColor = value;
        paintIndicatorBorder.setColor(value);
        invalidate();
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(@ColorInt int value) {
        fillColor = value;
        paintIndicatorFill.setColor(value);
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int value) {
        textColor = value;
        paintSpeed.setColor(value);
        paintMetric.setColor(value);
        invalidate();
    }

    // Paints
    private Paint paintIndicatorBorder = new Paint() {{
        setAntiAlias(true);
        setStyle(Style.STROKE);
        setColor(borderColor);
        setStrokeWidth(borderSize);
        setStrokeCap(Paint.Cap.ROUND);
    }};

    private Paint paintIndicatorFill = new Paint() {{
        setAntiAlias(true);
        setStyle(Style.STROKE);
        setColor(fillColor);
        setStrokeWidth(borderSize);
        setStrokeCap(Paint.Cap.ROUND);
    }};

    private Paint paintSpeed = new Paint() {{
        setAntiAlias(true);
        setStyle(Style.FILL);
        setColor(textColor);
        setTextSize(165f);
    }};

    private Paint paintMetric = new Paint() {{
        setAntiAlias(true);
        setStyle(Style.FILL);
        setColor(textColor);
        setTextSize(50f);
    }};

    // Animators
    private ValueAnimator animator = ValueAnimator.ofFloat();

    public SpeedometerLite(Context context) {
        super(context);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public SpeedometerLite(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainStyledAttributes(attrs, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public SpeedometerLite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttributes(attrs, defStyleAttr);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private void obtainStyledAttributes(AttributeSet attrs, int defStyleAttr) {
        // Obtain styled attributes and set the corresponding values
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        indicatorBorderRect.set(borderSize / 2, borderSize / 2, getWidth() - borderSize / 2, getWidth() - borderSize / 2);
        tickBorderRect.set(borderSize + TICK_MARGIN, borderSize + TICK_MARGIN, getWidth() - borderSize - TICK_MARGIN, getWidth() - borderSize - TICK_MARGIN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        renderBorder(canvas);
        renderBorderFill(canvas);
        paintSpeed.setColor(textColor);
        paintMetric.setColor(textColor);
        renderSpeedAndMetricText(canvas);
    }

    private void renderBorder(Canvas canvas) {
        canvas.drawArc(indicatorBorderRect, 140f, 260f, false, paintIndicatorBorder);
    }

    private void renderBorderFill(Canvas canvas) {
        canvas.drawArc(indicatorBorderRect, START_ANGLE, MIN_ANGLE - angle, false, paintIndicatorFill);
    }

    private void renderSpeedAndMetricText(Canvas canvas) {
        drawTextCentred(canvas,String.valueOf(speed), getWidth() / 2f, getHeight() / 2f, paintSpeed);
        drawTextCentred(canvas, metricText,getWidth() / 2f, getHeight() / 2f + paintSpeed.getTextSize() / 2 + textGap, paintMetric);
    }

    private float mapSpeedToAngle(int speed) {
        return (MIN_ANGLE + ((MAX_ANGLE - MIN_ANGLE) / (maxSpeed - MIN_SPEED)) * (speed - MIN_SPEED));
    }

    private int mapAngleToSpeed(float angle) {
        return (int) (MIN_SPEED + ((maxSpeed - MIN_SPEED) / (MAX_ANGLE - MIN_ANGLE)) * (angle - MIN_ANGLE));
    }

    public void setSpeed(int s, long d, Runnable onEnd) {
        animator.setFloatValues(mapSpeedToAngle(speed), mapSpeedToAngle(s));

        animator.addUpdateListener(animation -> {
            angle = (float) animation.getAnimatedValue();
            speed = mapAngleToSpeed(angle);
            invalidate();
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onEnd != null) {
                    onEnd.run();
                }
            }
        });

        animator.setDuration(d);
        animator.start();
    }

    private void drawTextCentred(Canvas canvas, String text, float cx, float cy, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
    }

    private float toRadian(float angle) {
        return (float) (angle * (Math.PI / 180));
    }

    private static final float MIN_ANGLE = 220f;
    private static final float MAX_ANGLE = -40f;
    private static final float START_ANGLE = 140f;
    private static final float MIN_SPEED = 0;
    private static final float TICK_MARGIN = 10f;
    private static final float TICK_TEXT_MARGIN = 30f;
    private static final float MAJOR_TICK_SIZE = 50f;
    private static final float MINOR_TICK_SIZE = 25f;
}

