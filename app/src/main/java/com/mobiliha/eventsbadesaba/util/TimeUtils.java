package com.mobiliha.eventsbadesaba.util;

import android.content.Context;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {

    public static final String TAG = "TimeUtils";

    /**
     * @param calendar Calendar
     * @return a string indicating hour and minute
     */
    public static String extractPersianTime(Calendar calendar) {
        String timeStr = new SimpleDateFormat("hh:mm").format(calendar.getTime());
        if(isLocaleIranAndFarsi())
            return toPersianNumber(timeStr);
        return timeStr;
    }

    /**
     * @param calendar Calendar
     * @return a string indication year, month and day
     */
    public static String extractPersianDate(Calendar calendar) {
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(calendar.getTimeInMillis());

        int year = persianCalendar.get(PersianCalendar.YEAR);
        int month = persianCalendar.get(PersianCalendar.MONTH);
        int day = persianCalendar.get(PersianCalendar.DAY_OF_MONTH);

        String monthName = ReminderApp.getAppContext().getResources().getStringArray(R.array.months)[month];
        if(isLocaleIranAndFarsi()) {
            return toPersianNumber(day) + " " + monthName + " " + toPersianNumber(year);
        }
        return year + " " + monthName + " " + day;
    }

    /**
     * @param number an string consisting of numbers
     * @return string of the given number with persian numbers
     */
    private static String toPersianNumber(String number) {
        char[] persianNumbers = {'۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'};

        for(int i = 0; i < 10; i++)
            number = number.replace((char) ('0' + i), persianNumbers[i]);

        return  number;
    }

    /**
     * @param number an integer
     * @return string of the given number with persian numbers
     */
    private static String toPersianNumber(int number) {
        return toPersianNumber(String.valueOf(number));
    }

    private static boolean isLocaleIranAndFarsi() {
        Context context = ReminderApp.getAppContext();
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.getLanguage().equals("fa") && locale.getCountry().equals("IR");
    }

}
