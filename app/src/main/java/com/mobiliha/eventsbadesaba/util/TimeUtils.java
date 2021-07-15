package com.mobiliha.eventsbadesaba.util;

import android.content.Context;
import android.content.res.Resources;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {

    public static final String TAG = "TimeUtils";

    /**
     * @param calendar Calendar.
     * @return A string indicating hour and minute.
     */
    public static String extractTime(Calendar calendar) {
        if(calendar == null)
            return null;

        String timeStr = new SimpleDateFormat("HH:mm").format(calendar.getTime());

        return timeStr;
    }

    public static PersianCalendar toPersianCalendar(Calendar calendar) {
        if(calendar == null)
            return null;

        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTime(calendar.getTime());
        return persianCalendar;
    }

    /**
     * @param calendar Calendar.
     * @return A string indication year, month and day.
     */
    public static String extractPersianDate(Calendar calendar) {
        if(calendar == null)
            return null;

        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(calendar.getTimeInMillis());

        int year = persianCalendar.get(PersianCalendar.YEAR);
        int month = persianCalendar.get(PersianCalendar.MONTH);
        int day = persianCalendar.get(PersianCalendar.DAY_OF_MONTH);

        Resources res = ReminderApp.getAppContext().getResources();

        if(isTomorrow(persianCalendar))
            return res.getString(R.string.tomorrow);

        if(isInTheNext6Days(persianCalendar)) {
            int dayIndex = persianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
            return res.getStringArray(R.array.week_days)[dayIndex];
        }

        String monthName = res.getStringArray(R.array.months)[month];

        if(isInTheSameYear(persianCalendar))
            return day + " " + monthName;

        return day + " " + monthName + " " + year;
    }

    /**
     * @param number On string consisting of numbers.
     * @return String of the given number with persian numbers.
     */
    private static String toPersianNumber(String number) {
        char[] persianNumbers = {'۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'};

        for(int i = 0; i < 10; i++)
            number = number.replace((char) ('0' + i), persianNumbers[i]);

        return  number;
    }

    /**
     * @param number An integer.
     * @return String of the given number with persian numbers.
     */
    private static String toPersianNumber(int number) {
        return toPersianNumber(String.valueOf(number));
    }

    private static boolean isLocaleIranAndFarsi() {
        Context context = ReminderApp.getAppContext();
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.getLanguage().equals("fa") && locale.getCountry().equals("IR");
    }

    private static boolean isTomorrow(PersianCalendar calendar) {
        PersianCalendar tomorrow = new PersianCalendar();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        return tomorrow.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && tomorrow.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isInTheSameYear(PersianCalendar calendar) {
        PersianCalendar now = new PersianCalendar();
        return now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
    }

    private static boolean isInTheNext6Days(PersianCalendar calendar) {
        PersianCalendar now = new PersianCalendar();
        int diff = calendar.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
        return now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && diff <= 6 && diff >= 1;
    }

}
