package com.mobiliha.eventsbadesaba.ui.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class FontIcon extends AppCompatTextView {

    public FontIcon(@NonNull Context context) {
        super(context);
        init();
    }

    public FontIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontIcon(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface typeface = Typeface.createFromAsset(
                getContext().getAssets(), "awesome_light_6_pro.otf"
        );
        setTypeface(typeface);
    }

}
