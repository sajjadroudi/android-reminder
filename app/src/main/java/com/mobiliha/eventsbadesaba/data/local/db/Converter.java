package com.mobiliha.eventsbadesaba.data.local.db;

import android.content.ContentValues;

import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;

import java.util.Calendar;

public class Converter {

    public static Task contentValuesToTask(ContentValues values) {
        if(values == null) return null;
        return new Task(
                values.getAsInteger(DbContract.TaskEntry.COL_NAME_TASK_ID),
                values.getAsString(DbContract.TaskEntry.COL_NAME_TITLE),
                values.getAsInteger(DbContract.TaskEntry.COL_NAME_DUE_DATE),
                values.getAsString(DbContract.TaskEntry.COL_NAME_OCCASION),
                values.getAsString(DbContract.TaskEntry.COL_NAME_DETAILS),
                values.getAsString(DbContract.TaskEntry.COL_NAME_LOCATION),
                values.getAsString(DbContract.TaskEntry.COL_NAME_LINK)
        );
    }

    public static ContentValues taskToContentValues(Task task) {
        if(task == null) return null;
        ContentValues values = new ContentValues();
        values.put(DbContract.TaskEntry.COL_NAME_TITLE, task.getTitle());
        values.put(DbContract.TaskEntry.COL_NAME_DUE_DATE, Converter.calendarToTimestamp(task.getDueDate()));
        values.put(DbContract.TaskEntry.COL_NAME_OCCASION, task.getOccasion());
        values.put(DbContract.TaskEntry.COL_NAME_DETAILS, task.getDetails());
        values.put(DbContract.TaskEntry.COL_NAME_LOCATION, task.getLocation());
        values.put(DbContract.TaskEntry.COL_NAME_LINK, task.getLink());
        return values;
    }

    public static Calendar timestampToCalendar(long value) {
        if(value == 0) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        return calendar;
    }

    public static long calendarToTimestamp(Calendar calendar) {
        if(calendar == null) return 0;
        return calendar.getTimeInMillis();
    }

}
