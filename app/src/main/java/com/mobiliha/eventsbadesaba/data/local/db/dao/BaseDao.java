package com.mobiliha.eventsbadesaba.data.local.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobiliha.eventsbadesaba.data.local.db.DbHelper;

public class BaseDao {

    private final DbHelper dbHelper;

    public BaseDao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Cursor query(
        String table,
        String selection,
        String[] selectionArgs,
        String sortOrder
    ) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(table, null, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor query(
            String table,
            String selection,
            String[] selectionArgs
    ) {
        return query(table, selection, selectionArgs, null);
    }

    public Cursor query(
        String table
    ) {
        return query(table, null, null, null);
    }

    public void insert(
        String table,
        ContentValues values
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(table, null, values);
    }

    public void update(
            String table,
            ContentValues values,
            String whereClause,
            String[] whereArgs
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(table, values, whereClause, whereArgs);
    }

    public void delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(table, whereClause, whereArgs);
    }

    public void delete(String table) {
        delete(table, null, null);
    }

}
