package com.mobiliha.eventsbadesaba.util;

import android.graphics.Typeface;

import com.mobiliha.eventsbadesaba.ReminderApp;

public class FontManager {

    public static final String AWESOME_FONT = "awesome_light_6_pro.otf";

    public static Typeface createTypeface(String font) {
        return Typeface.createFromAsset(
                ReminderApp.getAppContext().getAssets(),
                font
        );
    }

}
