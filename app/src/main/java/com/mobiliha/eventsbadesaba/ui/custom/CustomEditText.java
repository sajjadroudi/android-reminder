package com.mobiliha.eventsbadesaba.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.mobiliha.eventsbadesaba.R;

public class CustomEditText extends LinearLayout {

    public static final float DEFAULT_TEXT_SIZE = 17f;
    public static final int DEFAULT_TEXT_COLOR = Color.BLACK;

    private EditText edtMain;
    private FontIcon crossIcon;
    private FontIcon mainIcon;

    public CustomEditText(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Initialize layout
        setOrientation(LinearLayout.HORIZONTAL);

        TypedArray array = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomEditText, 0, 0
        );

        // Read attributes from xml
        String text = array.getString(R.styleable.CustomEditText_customEditTextText);
        String hintText = array.getString(R.styleable.CustomEditText_customEditTextTextHintText);
        float textSize = array.getDimension(R.styleable.CustomEditText_customEditTextTextTextSize, DEFAULT_TEXT_SIZE);
        int textColor = array.getColor(R.styleable.CustomEditText_customEditTextTextTextColor, DEFAULT_TEXT_COLOR);
        String icon = array.getString(R.styleable.CustomEditText_customEditTextIcon);

        array.recycle();

        // Inflate the layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_edit_text, this, true);

        // Initialize inner views
        edtMain = findViewById(R.id.edt_main);
        crossIcon = findViewById(R.id.icon_cross);
        mainIcon = findViewById(R.id.icon_main);

        // Initialize attributes
        setCustomEditTextText(text);
        setCustomEditTextTextHintText(hintText);
        setCustomEditTextTextTextSize(textSize);
        setCustomEditTextTextTextColor(textColor);
        setCustomEditTextIcon(icon);
        crossIcon.setOnClickListener(v -> setCustomEditTextText(null));

        edtMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                determineCrossIconVisibility();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void determineCrossIconVisibility() {
        boolean shouldShowCrossIcon = !edtMain.getText().toString().isEmpty();
        crossIcon.setVisibility(shouldShowCrossIcon ? View.VISIBLE : View.INVISIBLE);
    }

    public void setCustomEditTextText(String text) {
        edtMain.setText(text);

        determineCrossIconVisibility();
    }

    public String getCustomEditTextText() {
        return edtMain.getText().toString();
    }

    public void setCustomEditTextTextHintText(String text) {
        edtMain.setHint(text);
    }

    public String getCustomEditTextTextHintText() {
        return edtMain.getHint().toString();
    }

    public void setCustomEditTextTextTextSize(float size) {
        edtMain.setTextSize(size);
    }

    public void setCustomEditTextTextTextSize(int unit, float size) {
        edtMain.setTextSize(unit, size);
    }

    public float getCustomEditTextTextTextSize() {
        return edtMain.getTextSize();
    }

    public void setCustomEditTextTextTextColor(int color) {
        edtMain.setTextColor(color);
    }

    public int getCustomEditTextTextTextColor() {
        return edtMain.getCurrentTextColor();
    }

    public void addTextChangedListener(TextWatcher watcher) {
        edtMain.addTextChangedListener(watcher);
    }

    public void setCustomEditTextIcon(String icon) {
        mainIcon.setText(icon);

        boolean shouldIconBeVisible = icon != null && !icon.isEmpty();
        mainIcon.setVisibility(shouldIconBeVisible ? View.VISIBLE : View.GONE);
    }

    public String getCustomEditTextIcon() {
        return mainIcon.getText().toString();
    }

}
