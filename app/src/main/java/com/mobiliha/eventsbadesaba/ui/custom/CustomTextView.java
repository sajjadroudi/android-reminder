package com.mobiliha.eventsbadesaba.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobiliha.eventsbadesaba.R;

public class CustomTextView extends LinearLayout {

    public static final float DEFAULT_TEXT_SIZE = 17f;
    public static final int DEFAULT_TEXT_COLOR = Color.BLACK;

    private TextView txtMain;
    private FontIcon crossIcon;
    private FontIcon mainIcon;

    private String hintText;
    private boolean isHintMode;
    private int textColor;

    public CustomTextView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Initialize layout
        setOrientation(LinearLayout.HORIZONTAL);

        TypedArray array = getContext().obtainStyledAttributes(attrs,
                R.styleable.CustomTextView, 0, 0);

        // Read attributes from xml
        String mainText = array.getString(R.styleable.CustomTextView_customText);
        hintText = array.getString(R.styleable.CustomTextView_customHintText);
        float textSize = array.getDimension(R.styleable.CustomTextView_customTextSize, DEFAULT_TEXT_SIZE);
        int textColor = array.getColor(R.styleable.CustomTextView_customTextColor, DEFAULT_TEXT_COLOR);
        String icon = array.getString(R.styleable.CustomTextView_customIcon);

        array.recycle();

        // Inflate the layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_text_view, this, true);

        // Initialize inner views
        txtMain = findViewById(R.id.txt_main);
        crossIcon = findViewById(R.id.icon_cross);
        mainIcon = findViewById(R.id.icon_main);

        // Initialize attributes
        setCustomText(mainText);
        setCustomTextSize(textSize);
        setCustomTextColor(textColor);
        setCustomIcon(icon);
        crossIcon.setOnClickListener(v -> setCustomText(null));
        setHintMode(true);

    }

    private void setHintMode(boolean value) {
        isHintMode = value;

        int color;
        if(isHintMode) {
            color = getResources().getColor(R.color.hint);
            txtMain.setText(hintText);
            showCrossIcon(false);
        } else {
            color = textColor;
            showCrossIcon(true);
        }

        txtMain.setTextColor(color);
    }

    public void setCustomText(String text) {
        if(text == null || text.isEmpty()) {
            setHintMode(true);
        } else {
            txtMain.setText(text);
            setHintMode(false);
        }
    }

    public String getCustomText() {
        if(isHintMode)
            return null;
        return txtMain.getText().toString();
    }

    public void setCustomHintText(String hintText) {
        txtMain.setText(hintText);
    }

    public String getCustomHintText() {
        return hintText;
    }

    public void setCustomTextSize(float size) {
        txtMain.setTextSize(size);
    }

    public void setCustomTextSize(int unit, float size) {
        txtMain.setTextSize(unit, size);
    }

    public float getCustomTextSize() {
        return txtMain.getTextSize();
    }

    public void setCustomTextColor(int color) {
        textColor = color;
    }

    public int getCustomTextColor() {
        return txtMain.getCurrentTextColor();
    }

    private void showCrossIcon(boolean show) {
        crossIcon.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void setCustomIcon(String icon) {
        mainIcon.setText(icon);

        boolean shouldIconBeVisible = icon != null && !icon.isEmpty();
        mainIcon.setVisibility(shouldIconBeVisible ? View.VISIBLE : View.GONE);
    }

    public String getCustomIcon() {
        return mainIcon.getText().toString();
    }

}