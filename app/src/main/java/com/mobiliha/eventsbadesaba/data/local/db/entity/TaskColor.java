package com.mobiliha.eventsbadesaba.data.local.db.entity;

import android.graphics.Color;

public enum TaskColor {
    BLUE(Color.BLUE),
    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    RED(Color.RED);

    private final int colorCode;

    TaskColor(int colorCode) {
        this.colorCode = colorCode;
    }

    public int getColorCode() {
        return colorCode;
    }
}
