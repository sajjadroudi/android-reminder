package com.mobiliha.eventsbadesaba.data.local.db;

public class DbContract {

    private DbContract() {}

    public static class TaskEntry {
        public static final String TABLE_NAME = "task";
        public static final String COL_NAME_TASK_ID = "task_id";
        public static final String COL_NAME_TITLE = "title";
        public static final String COL_NAME_DUE_DATE = "due_date";
        public static final String COL_NAME_OCCASION = "occasion";
        public static final String COL_NAME_DETAILS = "details";
        public static final String COL_NAME_LOCATION = "location";
        public static final String COL_NAME_LINK = "link";
        public static final String COL_NAME_COLOR = "color";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_NAME_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        COL_NAME_TITLE + " TEXT, " +
                        COL_NAME_DUE_DATE + " INTEGER, " +
                        COL_NAME_OCCASION + " TEXT, " +
                        COL_NAME_DETAILS + " TEXT, " +
                        COL_NAME_LOCATION + " TEXT, " +
                        COL_NAME_LINK + " TEXT" +
                        COL_NAME_COLOR + " TEXT" +
                    ")";

        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}