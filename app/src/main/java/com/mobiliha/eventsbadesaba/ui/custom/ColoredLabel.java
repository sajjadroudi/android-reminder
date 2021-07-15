package com.mobiliha.eventsbadesaba.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.mobiliha.eventsbadesaba.R;

public class ColoredLabel extends View {

    private int color;
    private Paint paint;

    public ColoredLabel(Context context) {
        super(context);
        init(context, null);
    }

    public ColoredLabel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColoredLabel,
                0,
                0
        );

        try {
            color = array.getColor(R.styleable.ColoredLabel_labelColor, 0);
        } finally {
            array.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setLabelColor(int color) {
        this.color = color;
        invalidate();
        requestLayout();
    }

    public int getLabelColor() {
        return color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        Rect rect = new Rect(pl, pt, pl + usableWidth, pt + usableHeight);

        RectF rectF = new RectF(rect);

        paint.setColor(color);

        int r = Math.min(usableHeight, usableWidth);

        canvas.drawRoundRect(rectF, r, r, paint);
    }
}
