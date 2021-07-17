package com.mobiliha.eventsbadesaba.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mobiliha.eventsbadesaba.R;

public class CustomDateTimeView extends LinearLayout {

    private TextView txtDate;
    private TextView txtTime;
    private FontIcon mainIcon;

    public CustomDateTimeView(Context context) {
        super(context);
        init(null);
    }

    public CustomDateTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Initialize layout
        setOrientation(LinearLayout.HORIZONTAL);

        TypedArray array = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomDateTimeView, 0, 0
        );

        // Read attributes from xml
        String date = array.getString(R.styleable.CustomDateTimeView_customDateTimeDate);
        String time = array.getString(R.styleable.CustomDateTimeView_customDateTimeTime);
        String icon = array.getString(R.styleable.CustomDateTimeView_customDateTimeIcon);

        array.recycle();

        // Inflate the layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_date_time_view, this, true);

        // Initialize inner views
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        mainIcon = findViewById(R.id.icon_main);

        // Initialize attributes
        txtDate.setTextColor(CustomViewConstants.DEFAULT_TEXT_COLOR);
        txtTime.setTextColor(CustomViewConstants.DEFAULT_TEXT_COLOR);
        txtDate.setTextSize(CustomViewConstants.DEFAULT_TEXT_SIZE);
        txtTime.setTextSize(CustomViewConstants.DEFAULT_TEXT_SIZE);
        setCustomDateTimeDate(date);
        setCustomDateTimeTime(time);
        setCustomDateTimeIcon(icon);
    }

    public void setCustomDateTimeDate(String date) {
        txtDate.setText(date);
    }

    public String getCustomDateTimeDate() {
        return txtDate.getText().toString();
    }

    public void setCustomDateTimeTime(String time) {
        txtTime.setText(time);
    }

    public String getCustomDateTimeTime() {
        return txtTime.getText().toString();
    }

    public void setCustomDateTimeIcon(String icon) {
        mainIcon.setText(icon);
    }

    public String geCustomDateTimeIcon() {
        return mainIcon.getText().toString();
    }

}
