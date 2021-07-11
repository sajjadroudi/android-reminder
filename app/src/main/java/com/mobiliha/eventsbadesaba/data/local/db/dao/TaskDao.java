package com.mobiliha.eventsbadesaba.data.local.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.mobiliha.eventsbadesaba.data.local.db.Converter;
import com.mobiliha.eventsbadesaba.data.local.db.DbContract;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    private final BaseDao dao;
    private final String tableName = DbContract.TaskEntry.TABLE_NAME;

    public TaskDao(BaseDao dao) {
        this.dao = dao;
    }

    public List<Task> getAll() {
        Cursor cursor = dao.query(tableName);
        List<Task> tasks = extractTasks(cursor);
        cursor.close();
        return tasks;
    }

    public Task getTask(int taskId) {
        String selection = DbContract.TaskEntry.COL_NAME_TASK_ID + " = ?";
        String[] selectionArgs = { String.valueOf(taskId) };
        Cursor cursor = dao.query(tableName, selection, selectionArgs);
        cursor.moveToFirst();
        Task task = extractTask(cursor);
        cursor.close();
        return task;
    }

    public void insert(Task task) {
        ContentValues values = Converter.taskToContentValues(task);
        dao.insert(tableName, values);
    }

    public void update(Task task) {
        String whereClause = DbContract.TaskEntry.COL_NAME_TASK_ID + " = ?";
        String[] whereArgs = { String.valueOf(task.getTaskId()) };
        dao.update(tableName, Converter.taskToContentValues(task), whereClause, whereArgs);
    }

    public void delete(Task task) {
        String whereClause = DbContract.TaskEntry.COL_NAME_TASK_ID + " = ?";
        String[] whereArgs = { String.valueOf(task.getTaskId()) };
        dao.delete(tableName, whereClause, whereArgs);
    }

    private Task extractTask(Cursor cursor) {
        int taskId = cursor.getInt(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_TASK_ID));
        String title = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_TITLE));
        int dueDate = cursor.getInt(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_DUE_DATE));
        String occasion = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_OCCASION));
        String details = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_DETAILS));
        String location = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_LOCATION));
        String link = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_LINK));
        return new Task(taskId, title, dueDate, occasion, details, location, link);
    }

    private List<Task> extractTasks(Cursor cursor) {
        List<Task> tasks = new ArrayList<>();
        while(cursor.moveToNext()) {
            Task task = extractTask(cursor);
            tasks.add(task);
        }
        return tasks;
    }

}
