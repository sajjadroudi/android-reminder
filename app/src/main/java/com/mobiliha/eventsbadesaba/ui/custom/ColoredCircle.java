package com.mobiliha.eventsbadesaba.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.mobiliha.eventsbadesaba.R;

public class ColoredCircle extends View {

    public static final String TAG = "ColoredCircle";

    private float strokeWidth;
    private int color;
    private boolean isChosen;

    private Paint innerCirclePaint;
    private Paint outerCirclePaint;
    private int radius;
    private int centerX;
    private int centerY;

    public ColoredCircle(Context context) {
        super(context);
        init(context, null);
    }

    public ColoredCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColoredCircle,
                0,
                0
        );

        try {
            color = array.getColor(R.styleable.ColoredCircle_circleColor, 0);
            strokeWidth = array.getDimension(R.styleable.ColoredCircle_circleStrokeWidth, 0);
            isChosen = array.getBoolean(R.styleable.ColoredCircle_circleChosen, false);
        } finally {
            array.recycle();
        }

        innerCirclePaint = new Paint();
        innerCirclePaint.setAntiAlias(true);

        outerCirclePaint = new Paint();
        outerCirclePaint.setAntiAlias(true);
    }

    public void setCircleStrokeWidth(float value) {
        this.strokeWidth = value;
        invalidate();
        requestLayout();
    }

    public float getCircleStrokeWidth() {
        return strokeWidth;
    }

    public void setCircleColor(int color) {
        this.color = color;
        invalidate();
        requestLayout();
    }

    public int getCircleColor() {
        return color;
    }

    public void setCircleChosen(boolean chosen) {
        isChosen = chosen;
        invalidate();
        requestLayout();
    }

    public boolean isCircleChosen() {
        return isChosen;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        radius = Math.min(usableWidth, usableHeight) / 2;
        centerX = pl + (usableWidth / 2);
        centerY = pt + (usableHeight / 2);

        if(isChosen) {
            radius -= strokeWidth;
            outerCirclePaint.setColor(Color.BLACK);
            canvas.drawCircle(centerX, centerY, radius + strokeWidth, outerCirclePaint);
        }

        innerCirclePaint.setColor(color);
        canvas.drawCircle(centerX, centerY, radius, innerCirclePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float distance = (float) Math.sqrt(
                Math.pow(event.getX() - centerX, 2) +
                Math.pow(event.getY() - centerY, 2)
        );
        return distance <= radius;
    }

}
