package com.mobiliha.eventsbadesaba.data.local.db.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.mobiliha.eventsbadesaba.data.local.db.Converter;
import com.mobiliha.eventsbadesaba.data.local.db.DbContract;
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

    public Completable insert(Task task) {
        return Completable.create(emitter -> {
            try {
                ContentValues values = Converter.taskToContentValues(task);
                dao.insert(tableName, values);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Completable update(Task task) {
        return Completable.create(emitter -> {
           try {
               String whereClause = DbContract.TaskEntry.COL_NAME_TASK_ID + " = ?";
               String[] whereArgs = { String.valueOf(task.getTaskId()) };
               dao.update(tableName, Converter.taskToContentValues(task), whereClause, whereArgs);
               emitter.onComplete();
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
