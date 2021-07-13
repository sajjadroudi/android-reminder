package com.mobiliha.eventsbadesaba.util;

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

}
