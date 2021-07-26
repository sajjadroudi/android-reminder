package com.mobiliha.eventsbadesaba.data.local.db.entity;

import androidx.annotation.ColorInt;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;

public enum TaskColor {
    BLUE(ReminderApp.getAppContext().getResources().getColor(R.color.blue)),
    YELLOW(ReminderApp.getAppContext().getResources().getColor(R.color.yellow)),
    GREEN(ReminderApp.getAppContext().getResources().getColor(R.color.green)),
    RED(ReminderApp.getAppContext().getResources().getColor(R.color.red));

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
