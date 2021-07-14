package com.mobiliha.eventsbadesaba.data.local.db.entity;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public enum TaskColor {
    BLUE(Color.BLUE),
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
