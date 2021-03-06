package com.mobiliha.eventsbadesaba.data.local.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.mobiliha.eventsbadesaba.data.local.db.Converter;
import com.mobiliha.eventsbadesaba.data.local.db.DbContract;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class TaskDao {

    private final BaseDao dao;
    private final String tableName = DbContract.TaskEntry.TABLE_NAME;

    public TaskDao(BaseDao dao) {
        this.dao = dao;
    }

    public Single<List<Task>> getAllTasks() {
        return Single.create(emitter -> {
            String sortOrder = DbContract.TaskEntry.COL_NAME_TASK_ID;
            try (Cursor cursor = dao.query(tableName, null, null, sortOrder)) {
                List<Task> tasks = extractTasks(cursor);
                emitter.onSuccess(tasks);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Task> getTask(int taskId) {
        return Single.create(emitter -> {
            String selection = DbContract.TaskEntry.COL_NAME_TASK_ID + " = ?";
            String[] selectionArgs = { String.valueOf(taskId) };

            try(Cursor cursor = dao.query(tableName, selection, selectionArgs)) {
                cursor.moveToFirst();
                Task task = extractTask(cursor);
                emitter.onSuccess(task);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Task> insert(Task task) {
        return Single.create(emitter -> {
            try {
                ContentValues values = Converter.taskToContentValues(task);
                long taskId = dao.insert(tableName, values);

                if(taskId > Integer.MAX_VALUE)
                    throw new IllegalStateException("taskId is too big.");

                Task insertedTask = new Task((int) taskId, task);

                emitter.onSuccess(insertedTask);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Task> update(Task task) {
        return Single.create(emitter -> {
           try {
               String whereClause = DbContract.TaskEntry.COL_NAME_TASK_ID + " = ?";
               String[] whereArgs = { String.valueOf(task.getTaskId()) };
               dao.update(tableName, Converter.taskToContentValues(task), whereClause, whereArgs);
               emitter.onSuccess(task);
           } catch (Exception e) {
               emitter.onError(e);
           }
        });
    }

    public Completable delete(Task task) {
        return Completable.create(emitter -> {
            try {
                String whereClause = DbContract.TaskEntry.COL_NAME_TASK_ID + " = ?";
                String[] whereArgs = { String.valueOf(task.getTaskId()) };
                dao.delete(tableName, whereClause, whereArgs);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Completable deleteAll() {
        return Completable.create(emitter -> {
           try {
               dao.delete(tableName);
               emitter.onComplete();
           } catch (Exception e) {
               emitter.onError(e);
           }
        });
    }

    private Task extractTask(Cursor cursor) {
        int taskId = cursor.getInt(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_TASK_ID));
        String title = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_TITLE));
        long dueDate = cursor.getLong(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_DUE_DATE));
        String occasion = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_OCCASION));
        String details = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_DETAILS));
        String location = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_LOCATION));
        String link = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_LINK));
        String color = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_COLOR));
        String token = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_TOKEN));
        String shareId = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_SHARE_ID));
        String shareLink = cursor.getString(cursor.getColumnIndex(DbContract.TaskEntry.COL_NAME_SHARE_LINK));
        return new Task(taskId, title, dueDate, occasion, details, location, link, color, token, shareId, shareLink);
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
