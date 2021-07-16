package com.mobiliha.eventsbadesaba.ui.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.mobiliha.eventsbadesaba.util.FontManager;

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
        Typeface typeface = FontManager.createTypeface(FontManager.AWESOME_FONT);
        setTypeface(typeface);
    }

}
