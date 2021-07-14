package com.mobiliha.eventsbadesaba.util;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;

public class Utils {

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Try to trim the input string, if the string got empty return null otherwise the result.
     * @param input Input string.
     * @return If the input string can be trimmed return trimmed version otherwise null.
     */
    public static String tryTrim(String input) {
        if(input == null || input.trim().isEmpty())
            return null;
        return input.trim();
    }

    public static String toLocaleString(Occasion occasion) {
        String[] occasions = ReminderApp.getAppContext()
                .getResources().getStringArray(R.array.occasions);

        String occasionStr = occasions[occasion.ordinal()];

        return occasionStr;
    }

}
