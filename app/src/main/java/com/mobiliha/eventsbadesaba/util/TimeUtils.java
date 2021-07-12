package com.mobiliha.eventsbadesaba.util;

import java.util.Calendar;

public class TimeUtils {

    /**
     * @param calendar Calendar
     * @return something like:
     * ۱۳:۴۷
     */
    public static String extractPersianTime(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return toPersianNumber(hour) + ":" + toPersianNumber(minute);
    }

    /**
     * @param calendar Calendar
     * @return something like:
     *   ۲۱ تیر ۱۴۰۰
     */
    public static String extractPersianDate(Calendar calendar) {
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(calendar.getTimeInMillis());

        int year = persianCalendar.get(PersianCalendar.YEAR);
        int month = persianCalendar.get(PersianCalendar.MONTH);
        int day = persianCalendar.get(PersianCalendar.DAY_OF_MONTH);

        return toPersianNumber(day) +
                " " +
                getMonthName(month) +
                " " +
                toPersianNumber(year);
    }

    /**
     * @param month number of month starting from zero.
     * @return persian name of the corresponding month number.
     */
    private static String getMonthName(int month) {
        switch (month) {
            case 0:
                return "فروردین";
            case 1:
                return "اردیبهشت";
            case 2:
                return "خرداد";
            case 3:
                return "تیر";
            case 4:
                return "مرداد";
            case 5:
                return "شهریور";
            case 6:
                return "مهر";
            case 7:
                return "آبان";
            case 8:
                return "آذر";
            case 9:
                return "دی";
            case 10:
                return "بهمن";
            case 11:
                return "اسفند";
        }

        throw new IllegalArgumentException();
    }

    /**
     * @param number an integer
     * @return string of the given number with persian numbers
     */
    private static String toPersianNumber(int number) {
        String numberStr = String.valueOf(number);

        char[] persianNumbers = {'۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'};

        for(int i = 0; i < 10; i++) {
            numberStr = numberStr.replace((char) ('0' + i), persianNumbers[i]);
        }

        return  numberStr;
    }

}
