package com.mobiliha.eventsbadesaba.ui.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.mobiliha.eventsbadesaba.R;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Typeface typeface = getResources().getFont(R.font.awesome_light_6_pro);
            setTypeface(typeface);
        }
    }

}
