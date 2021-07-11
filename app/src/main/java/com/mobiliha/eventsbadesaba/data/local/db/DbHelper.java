package com.mobiliha.eventsbadesaba.data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobiliha.eventsbadesaba.data.local.db.dao.BaseDao;
import com.mobiliha.eventsbadesaba.data.local.db.dao.TaskDao;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "database.sqlite";
    public static final int DB_VERSION = 1;

    private static DbHelper instance;

    public TaskDao getTaskDao() {
        return new TaskDao(new BaseDao(this));
    }

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DbHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTaskTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop all tables
        dropTaskTable(db);

        // Recreate them
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void createTaskTable(SQLiteDatabase db) {
        String query = DbContract.TaskEntry.SQL_CREATE_TABLE;
        db.execSQL(query);
    }

    private void dropTaskTable(SQLiteDatabase db) {
        String query = DbContract.TaskEntry.SQL_DROP_TABLE;
        db.execSQL(query);
    }

}
