package com.mobiliha.eventsbadesaba.data.local.db.entity;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;

public enum TaskColor {
    BLUE(ReminderApp.getAppContext().getResources().getColor(R.color.blue)),
    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    RED(Color.RED);

    @ColorInt
    private final int colorCode;

    TaskColor(@ColorInt int colorCode) {
        this.colorCode = colorCode;
    }

    @ColorInt
    public int getColorCode() {
        return colorCode;
    }

    public static TaskColor getAssociatedTaskColor(@ColorInt int colorCode) {
        for(TaskColor color : values()) {
            if(color.colorCode == colorCode)
                return color;
        }
        return null;
    }
}
