package com.mobiliha.eventsbadesaba.data.local.db;

import android.content.ContentValues;

import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;

import java.util.Calendar;

public class Converter {

    public static ContentValues taskToContentValues(Task task) {
        if(task == null) return null;
        ContentValues values = new ContentValues();
        long timestamp = Converter.calendarToTimestamp(task.getDueDate());
        String occasionStr = (task.getOccasion() == null) ? null : task.getOccasion().toString();
        values.put(DbContract.TaskEntry.COL_NAME_TITLE, task.getTitle());
        values.put(DbContract.TaskEntry.COL_NAME_DUE_DATE, timestamp);
        values.put(DbContract.TaskEntry.COL_NAME_OCCASION, occasionStr);
        values.put(DbContract.TaskEntry.COL_NAME_DETAILS, task.getDetails());
        values.put(DbContract.TaskEntry.COL_NAME_LOCATION, task.getLocation());
        values.put(DbContract.TaskEntry.COL_NAME_LINK, task.getLink());
        values.put(DbContract.TaskEntry.COL_NAME_COLOR, task.getColor().toString());
        return values;
    }

    public static Calendar timestampToCalendar(long value) {
        if(value == 0)
            return null;
        if(value < 0)
            throw new IllegalArgumentException("Timestamp can never be negative.");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);

        return calendar;
    }

    public static long calendarToTimestamp(Calendar calendar) {
        if(calendar == null) return 0;
        return calendar.getTimeInMillis();
    }

}
